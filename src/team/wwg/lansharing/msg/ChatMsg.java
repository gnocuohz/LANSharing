package team.wwg.lansharing.msg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import team.wwg.lansharing.user.UserInfo;
import team.wwg.lansharing.util.BaseRequest;
import team.wwg.lansharing.util.BytesUtil;

public class ChatMsg implements BaseMsg{

	private UserInfo info;
	private String chatmsg;
	
	public ChatMsg(UserInfo info, String chatmsg) {
		super();
		this.info = info;
		this.chatmsg = chatmsg;
	}
	
	public ChatMsg(byte[] message) {
		
		formBytes(message);
	}
	
	public UserInfo getInfo() {
		return info;
	}
	public void setInfo(UserInfo info) {
		this.info = info;
	}
	public String getChatmsg() {
		return chatmsg;
	}
	public void setChatmsg(String chatmsg) {
		this.chatmsg = chatmsg;
	}
	

	public byte[] toBytes(){
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

	
		byte[] byte_msgType = null;
		
		byte[] byteUserinfo = null;
		byte[] byteUserinfoLenth = null;
		
		byte[] bytechatMsg = null;
		byte[] byteMsgLenth = null;
		
		byte[] byteMainMsg = null;
		byte[] byteMainMsgLenth = null;
		int MainmsgLenth = 0;
		
		try {

			byte_msgType = BytesUtil.intToByteArray(BaseRequest.MSG_TYPE_CHAT_MESSAGE);
			
			byteUserinfo = this.info.toBytes();
			byteUserinfoLenth  = BytesUtil.intToByteArray(byteUserinfo.length);
			bytechatMsg = this.chatmsg.getBytes("UTF-8");
			
			
			
			// 获取长度计算
//			 byteIPAdress.length+
			byteMsgLenth = BytesUtil.intToByteArray(bytechatMsg.length);
			MainmsgLenth = byteUserinfo.length+bytechatMsg.length+byteUserinfoLenth.length;
			
			byteMainMsgLenth = BytesUtil.intToByteArray(MainmsgLenth);
			
			// 写入流
			arrayOutputStream.write(byte_msgType);
			arrayOutputStream.write(byteMainMsgLenth);
			
			arrayOutputStream.write(byteUserinfoLenth);
			arrayOutputStream.write(byteUserinfo);
		
			arrayOutputStream.write(byteMsgLenth);
			arrayOutputStream.write(bytechatMsg);
			
			byteMainMsg = arrayOutputStream.toByteArray();
			
			arrayOutputStream.flush();
			arrayOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteMainMsg;
		
	}
		
	
	public void formBytes(byte[] data){
		
		ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
		
		
		byte[] byte_msgType = new byte[4];
		byte[] byteMsgLenth = new byte[4];
		byte[] byteUserinfoLenth = new byte[4];
		byte[] byteChatMsgLenth = new byte[4];
		
		
		
		arrayInputStream.read(byte_msgType, 0, 4);
		arrayInputStream.read(byteMsgLenth, 0, 4);
		
		
	
		int main_msg_len = BytesUtil.byteArrayToInt(byteMsgLenth);
		int _msgType = BytesUtil.byteArrayToInt(byte_msgType);
		
		
		arrayInputStream.read(byteUserinfoLenth, 0, 4);
		int userLenth = BytesUtil.byteArrayToInt(byteUserinfoLenth);	
		byte[] byteUserinfo =  new byte[userLenth];
		arrayInputStream.read(byteUserinfo, 0, userLenth);
		info = new UserInfo("", "", "");
		this.info.fromBytes(byteUserinfo);
		
		
		
		arrayInputStream.read(byteChatMsgLenth, 0, 4);
		int chatMsglenth = BytesUtil.byteArrayToInt(byteChatMsgLenth);
		byte[] byteChatMsg =  new byte[chatMsglenth];
		arrayInputStream.read(byteChatMsg, 0, chatMsglenth);
		try {
			this.chatmsg = new String(byteChatMsg,"UTF-8");
			arrayInputStream.close();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	
	
}
