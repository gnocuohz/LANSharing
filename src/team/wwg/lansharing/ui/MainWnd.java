package team.wwg.lansharing.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import team.wwg.lansharing.manager.MessageManager;
import team.wwg.lansharing.msg.ChangeNickNameMsg;
import team.wwg.lansharing.msg.LogOutMsg;
import team.wwg.lansharing.msg.LoginMsg;
import team.wwg.lansharing.msg.WaitingSendMsg;
import team.wwg.lansharing.service.IUpdateOnlineUserNum;
import team.wwg.lansharing.service.ServerControl;
import team.wwg.lansharing.util.BaseRequest;
import team.wwg.lansharing.util.LocalInfoUtil;

public class MainWnd {
	
	private JFrame mainFrame =new JFrame();
	public static JTextField nickName = new JTextField("用户");
	private JLabel numLabel = null;
	private int windowWidth = 300;      
	private int windowHeight = 480;
	private JButton btnRename = null;
	private OnlineUserTable onlineUserTable = null;
	private MessageManager messageManager = null;
	public MainWnd()
	{
		messageManager = MessageManager.getInstance();
		
		
		init();
	}

	private void init() {
		mainFrame.setTitle("LANSharing");
		mainFrame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				LoginMsg loginMsg = new LoginMsg(LocalInfoUtil.getSelfInfo());
				messageManager.addWaitSendMsg(new WaitingSendMsg(loginMsg.toBytes(), "224.0.0.2",BaseRequest.MULTICAST_PORT));
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
				LogOutMsg loginoutMsg = new LogOutMsg(LocalInfoUtil.getSelfInfo());
				messageManager.addWaitSendMsg(new WaitingSendMsg(loginoutMsg.toBytes(), "224.0.0.2",BaseRequest.MULTICAST_PORT));
				System.exit(0);
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
		nickName.setEditable(false);
		mainFrame.setSize(windowWidth, windowHeight);
		mainFrame.setVisible(true);
	}

	//初始化界面位置
	private void initStartPos()
	{
		int  hight=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		int  width=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		mainFrame.setLocation((width-windowWidth)/2, (hight-windowHeight)/2);
	}
	//初始化界面
	private void initArea() {
		JPanel mainArea = new JPanel(new BorderLayout());
		
		initButton(mainArea);
		initSelfInfoPanel(mainArea);
		initScrollPane(mainArea);
		
		mainFrame.add(mainArea);
	}
	
	private void initSelfInfoPanel(JPanel mainArea){
		JLabel iconLabel = new JLabel();
		System.out.println(getClass().getResource("/res/person.png"));
		Icon icon=new ImageIcon(getClass().getResource("/res/person.png")); 
		iconLabel.setIcon(icon);
		JLabel label = new JLabel("昵称：");
		JLabel peolabel = new JLabel("   在线人数：");
		numLabel = new JLabel("0");
		JPanel selfInfoPanel=new JPanel(new FlowLayout());
		FlowLayout flowlayout = new FlowLayout();
		selfInfoPanel.setLayout(flowlayout);
		
		selfInfoPanel.add(iconLabel);
		selfInfoPanel.add(label);
		selfInfoPanel.add(nickName);
		selfInfoPanel.add(btnRename);
		selfInfoPanel.add(peolabel);
		selfInfoPanel.add(numLabel);
		mainArea.add(selfInfoPanel,BorderLayout.NORTH);
	}
	
	private void initScrollPane(JPanel mainArea){
		JScrollPane scrollpane = new JScrollPane();
		onlineUserTable = new OnlineUserTable();
		onlineUserTable.setUpdateUserNum(new IUpdateOnlineUserNum() {
			
			@Override
			public void updateUserNum(int num) {
				SwingUtilities.invokeLater(new Runnable()
				{  
				      @Override 
				      public void run() 
				      {
				    	  numLabel.setText(num+"");
				      }  
				});
			}
		});
		scrollpane.setViewportView(onlineUserTable.getOnlineUserTable());
		mainArea.add(scrollpane,BorderLayout.CENTER);
	}
	
	
	private void initButton(JPanel mainArea){
		JButton btnRefresh = new JButton("刷新");
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onlineUserTable.delAllInfo();
				LoginMsg loginMsg = new LoginMsg(LocalInfoUtil.getSelfInfo());
				messageManager.addWaitSendMsg(new WaitingSendMsg(loginMsg.toBytes(), "224.0.0.2",BaseRequest.MULTICAST_PORT));
			}
		});
		
		btnRename = new JButton();
		btnRename.setPreferredSize(new Dimension(20, 20));
		btnRename.setIcon(new ImageIcon(getClass().getResource("/res/rename.png")));
		btnRename.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(nickName.isEditable())
				{
					if(nickName.getText().length()>4)
					{
						JOptionPane.showMessageDialog(null, "昵称最大长度为8字符"
								, "昵称长度", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						SwingUtilities.invokeLater(new Runnable() {
						
							@Override
							public void run() {
								// TODO Auto-generated method stub
								nickName.setEditable(false);
								
							}
						});
						ChangeNickNameMsg changeNickNameMsg = new ChangeNickNameMsg(LocalInfoUtil.getSelfInfo());
						messageManager.addWaitSendMsg(new WaitingSendMsg(changeNickNameMsg.toBytes(), "224.0.0.2",BaseRequest.MULTICAST_PORT));
					}
				}
				else
				{
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							
							nickName.setEditable(true);
						}
					});
				}
			}
		});
		mainArea.add(btnRefresh,BorderLayout.SOUTH);
	}
}
