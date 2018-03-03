package team.wwg.lansharing.msg;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import team.wwg.lansharing.manager.ChatWndManager;
import team.wwg.lansharing.manager.MainWndManager;
import team.wwg.lansharing.manager.MessageManager;
import team.wwg.lansharing.task.TCPFileReceiverTask;
import team.wwg.lansharing.util.BaseRequest;
import team.wwg.lansharing.util.BytesUtil;
import team.wwg.lansharing.util.FileUtil;
import team.wwg.lansharing.util.LocalInfoUtil;

public class MsgParserHandler {
	private MessageManager messageManager = null;
	private MainWndManager mainWndManager = null;
	private ChatWndManager chatWndManager = null;
	private Selector selector = null;
	public MsgParserHandler() {
		mainWndManager = MainWndManager.getInstance();
		messageManager = MessageManager.getInstance();
		chatWndManager = ChatWndManager.getInstance();
	}
	
	public MsgParserHandler(Selector selector) {
		mainWndManager = MainWndManager.getInstance();
		messageManager = MessageManager.getInstance();
		chatWndManager = ChatWndManager.getInstance();
		this.selector = selector;
	}
	public void parsingMsg(ByteBuffer message){
		byte[] messageByte = message.array();
		
		int type = BytesUtil.byteArrayToInt(messageByte);
		
		switch (type) {
		case BaseRequest.MSG_TYPE_LOGIN:
			LoginMsg loginMsg = new LoginMsg(messageByte);
			mainWndManager.invokingUpdateOnlineUser(loginMsg.getInfo());
			//回送
			CallBackMsg localCallBackMsg = new CallBackMsg(LocalInfoUtil.getSelfInfo());
			messageManager.addWaitSendMsg(new WaitingSendMsg(localCallBackMsg.toBytes(), "224.0.0.2",BaseRequest.MULTICAST_PORT));
			break;
		case BaseRequest.MSG_TYPE_LOGOUT:
			LogOutMsg logOutMsg = new LogOutMsg(messageByte);
			mainWndManager.invokingRemoveOnlineUser(logOutMsg.getInfo().getStrIPAddress());
			break;
		case BaseRequest.MSG_TYPE_LOGIN_CALLBACK:
			CallBackMsg callBackMsg = new CallBackMsg(messageByte);
			mainWndManager.invokingUpdateOnlineUser(callBackMsg.getInfo());
			break;	
		case BaseRequest.MSG_TYPE_CHAT_MESSAGE:
			ChatMsg chatMsg = new ChatMsg(messageByte);
			chatWndManager.updateChatWndMsgArea(chatMsg);
			break;
		case BaseRequest.MSG_TYPE_SEND_FILE:
			SendFileMsg sendFileMsg = new SendFileMsg(messageByte);
			chatWndManager.updateChatWndFileMsg(sendFileMsg);
			int n =JOptionPane.showOptionDialog(null,
					"确定接受:"+FileUtil.getFileAllName(sendFileMsg.getFileName())+"该文件吗? 大小:"+FileUtil.showFilelength(sendFileMsg.getFilelength()), "收到对方的文件接收请求",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE,null,BaseRequest.options,BaseRequest.options[0]); 
			
			if(n==0){

				TCPFileReceiverTask client = new TCPFileReceiverTask(sendFileMsg.getInfo().getStrIPAddress(), selector, sendFileMsg.getFileName());
				
			}else if(n==1){
				JFileChooser fileChooer = new JFileChooser();
				fileChooer.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooer.showDialog(new JLabel(), "文件路径选择");
				
				File file = fileChooer.getSelectedFile();
				if(file==null){
					
					JOptionPane.showMessageDialog(null, "未选择文件路径，请重试！"
							, "文件信息", JOptionPane.ERROR_MESSAGE);
					return;
		
				}
				String  newfilePath =file.getAbsolutePath();
				if(newfilePath!=null){
					TCPFileReceiverTask client = new TCPFileReceiverTask(sendFileMsg.getInfo().getStrIPAddress(), selector, sendFileMsg.getFileName(),newfilePath);
				}
			
			}
			break;
		case BaseRequest.MSG_TYPE_CHANGENICKNAME:
			ChangeNickNameMsg changeNickNameMsg = new ChangeNickNameMsg(messageByte);
			mainWndManager.invokingUpdateOnlineUser(changeNickNameMsg.getInfo());
			break;
		default:
			break;
		}
		
	}
}
