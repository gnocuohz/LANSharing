package team.wwg.lansharing.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.nio.channels.SelectionKey;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import team.wwg.lansharing.manager.ChatWndManager;
import team.wwg.lansharing.manager.MessageManager;
import team.wwg.lansharing.manager.TaskManager;
import team.wwg.lansharing.msg.ChatMsg;
import team.wwg.lansharing.msg.SendFileMsg;
import team.wwg.lansharing.msg.WaitingSendMsg;
import team.wwg.lansharing.user.UserInfo;
import team.wwg.lansharing.util.BaseRequest;
import team.wwg.lansharing.util.FileUtil;
import team.wwg.lansharing.util.LocalInfoUtil;

public class ChatWnd {
	
	UserInfo userInfo;
	JFrame chatFrame =new JFrame();
	int windowWidth = 500;
	int windowHeight = 500;

	private JTextArea topArea;
	private JTextArea  bottomArea;
//	private JLabel Progressbar;
	private JScrollPane jscrollpane;
	private JButton btnSendMessage = new JButton("发送(Enter)");
	private JButton btnSendFile = new JButton("选择文件(F)");

	final KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
	
	private MessageManager messageManager = null;
	
	private ChatWndManager chatWndManager = null;
	
	public ChatWnd(UserInfo userInfo){
		this.userInfo = userInfo;
		init();
	}
	
	private void init(){
		messageManager = MessageManager.getInstance();
		chatWndManager = ChatWndManager.getInstance();
		chatFrame.setTitle(userInfo.getStrNickName()+"("+userInfo.getStrIPAddress()+")");
		chatFrame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
			
				bottomArea.requestFocus();
				topArea.setEditable(false);
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
			
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
			
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				
				chatWndManager.removeChatWnd(userInfo.getStrIPAddress());
				chatFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
			
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				
				
			}
		});
		initStartPos();
		initArea();
		initBotton();
		chatFrame.setSize(windowWidth, windowHeight);
		chatFrame.setVisible(true);
	}
	
	

	private void initStartPos() {
		int hight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int width=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		chatFrame.setLocation((width-windowWidth)/2, (hight-windowHeight)/2);
	}
	
	private void initArea() {
		
		JSplitPane splistPane = new JSplitPane();
		splistPane.setOneTouchExpandable(true);
		splistPane.setContinuousLayout(true);
		splistPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splistPane.setDividerSize(10);
		splistPane.setDividerLocation(300);
		initTopArea(splistPane);
		initBottomArea(splistPane);
		chatFrame.setContentPane(splistPane);
		
	}
	
	private void initBotton() {
		
		btnSendMessage.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		
				final String text = bottomArea.getText();
				if(!text.equals("")){
					/**
					 * 发送消息！！！！！！
					 */
					ChatMsg chatMessage = new ChatMsg(LocalInfoUtil.getSelfInfo(), text);
					
					messageManager.addWaitSendMsg(new WaitingSendMsg(chatMessage.toBytes(), userInfo.getStrIPAddress(), BaseRequest.UDP_PORT));
					
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Date time = new Date();
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									topArea.append(time+"--"+userInfo.getStrNickName()+"\r\n"+text+"\r\n");
								}
								
							});
							bottomArea.setText("");
						}
					});
					
					
				}
			}
		});
		
		btnSendFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				openFileChooser();
				System.out.println("send  点击！");
			}
			
		});
	}
	
	
	private void openFileChooser() {

		JFileChooser fileChooer = new JFileChooser();
		fileChooer.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooer.showDialog(new JLabel(), "文件选择");
		
		
		File file = fileChooer.getSelectedFile();
		String fileMsg = null;
	
		if (null == file) {
			JOptionPane.showMessageDialog(null, "未选择文件", "文件信息", JOptionPane.ERROR_MESSAGE);
			//int n = JOptionPane.showConfirmDialog(null, "你高兴吗?", "标题",JOptionPane.YES_NO_OPTION);
		
			return;
		}

		if (file.isDirectory()) {
//			System.out.println("文件夹:" + file.getAbsolutePath());
//			fileMsg = "文件夹:" + file.getAbsolutePath();
			
			JOptionPane.showMessageDialog(null, "请勿发送文件夹", "文件信息", JOptionPane.ERROR_MESSAGE);
			//int n = JOptionPane.showConfirmDialog(null, "你高兴吗?", "标题",JOptionPane.YES_NO_OPTION);
		
			return;
		} else if (file.isFile()) {
			System.out.println("文件:" + file.getAbsolutePath());
			fileMsg = file.getAbsolutePath();
		}
		System.out.println(fileChooer.getSelectedFile().getName());
		//确定返回  0, 否则返回1
		int n = JOptionPane.showConfirmDialog(null, "你确定要发送:  "+FileUtil.getFileAllName(fileMsg)+"   该文件吗?", "发送文件提示",JOptionPane.YES_NO_OPTION);
		
		if(n==0){
			SendFileMsg sendFileMsg = new SendFileMsg(fileMsg, userInfo, file.length());
			messageManager.addWaitSendMsg(new WaitingSendMsg(sendFileMsg.toBytes(), userInfo.getStrIPAddress(), BaseRequest.UDP_PORT));
		}
	}
	
	private void initTopArea(JSplitPane splistPane) {
		// TODO Auto-generated method stub
		topArea = new JTextArea();
		jscrollpane = new JScrollPane();
		jscrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel jTopAreaPanle = new JPanel();
		
		BoxLayout boxLayout = new BoxLayout(jTopAreaPanle, BoxLayout.X_AXIS);
		jTopAreaPanle.setLayout(boxLayout);
		topArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
		jscrollpane.setViewportView(topArea);
		jTopAreaPanle.add(jscrollpane);
		JPanel topsplistPane = new JPanel(new BorderLayout());
		topsplistPane.add(jTopAreaPanle,BorderLayout.CENTER);
//		Progressbar = new JLabel("123");
//		topsplistPane.add(Progressbar,BorderLayout.SOUTH);
		splistPane.setTopComponent(topsplistPane);
	}
	
	private void initBottomArea(JSplitPane splistPane) {
		// TODO Auto-generated method stub
		bottomArea = new JTextArea() {
			protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
				final String text = bottomArea.getText();
				if (ks.equals(enterKey)) 
				{
					if(!text.equals("")){
						
						
						/**
						 * 发送消息
						 * 
						 */
						ChatMsg chatMessage = new ChatMsg(LocalInfoUtil.getSelfInfo(), text);
						
						messageManager.addWaitSendMsg(new WaitingSendMsg(chatMessage.toBytes(), userInfo.getStrIPAddress(), BaseRequest.UDP_PORT));
						
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Date time = new Date();
								SwingUtilities.invokeLater(new Runnable() {

									@Override
									public void run() {
										topArea.append(time+"--"+userInfo.getStrNickName()+"\r\n"+text+"\r\n");
									}
									
								});
								bottomArea.setText("");
							}
						});
						
						return true;
					}
					return false;
					}
				return super.processKeyBinding(ks, e, condition, pressed);
				}
			};
		JPanel jBottomAreaPanle = new JPanel();
		BoxLayout boxLayout = new BoxLayout(jBottomAreaPanle, BoxLayout.Y_AXIS);
		jBottomAreaPanle.setLayout(boxLayout);
		
		JPanel buttonPanel = new JPanel();
//		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		BoxLayout rightayout = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
		buttonPanel.setLayout(rightayout);
		buttonPanel.add(btnSendFile);
		buttonPanel.add(btnSendMessage);
		bottomArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,2));
		jBottomAreaPanle.add(bottomArea);
		jBottomAreaPanle.add(buttonPanel);
		splistPane.setBottomComponent(jBottomAreaPanle);
	}

	public JTextArea getJTextArea()
	{
		return topArea;
	}
	
	public void updateMessage(ChatMsg chatMsg){
		Date time = new Date();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				topArea.append(time+"--"+chatMsg.getInfo().getStrNickName()+"\r\n"+chatMsg.getChatmsg()+"\r\n");
			}
			
		});
	}
	
	public void updateFileMessage(SendFileMsg sendFileMsg){
		Date time = new Date();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				topArea.append(time+"--"+sendFileMsg.getInfo().getStrNickName()+
						"\r\n"+FileUtil.getFileAllName(sendFileMsg.getFileName())+"文件长度："+FileUtil.showFilelength(sendFileMsg.getFilelength())+"\r\n");
			}
			
		});
	}
	
	public JScrollPane getScrollPane(){
		return jscrollpane;
	}
}
