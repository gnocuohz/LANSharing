package team.wwg.lansharing.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import team.wwg.lansharing.manager.ChatWndManager;
import team.wwg.lansharing.manager.MainWndManager;
import team.wwg.lansharing.service.IUpdateOnlineUserNum;
import team.wwg.lansharing.service.IUpdateOnlineUserTable;
import team.wwg.lansharing.user.UserInfo;

public class OnlineUserTable {
	
	private IUpdateOnlineUserNum updateOnlineUserNum = null;
	
	private static HashMap<String, UserInfo> onlineUsersMap = null;
	
	private JTable tblOnlineUser = null;
	private DefaultTableModel tblModle = null;
	private MainWndManager updateUIManager = null;
	private ChatWndManager chatWndManager = null;
			
	public OnlineUserTable(){
		onlineUsersMap = new HashMap<String, UserInfo>();
		initOnlineUserMap();
		initTable();
		initUpdateUIManager();
	}
	
	private void initUpdateUIManager()
	{
		chatWndManager = ChatWndManager.getInstance();
		updateUIManager = MainWndManager.getInstance();
		updateUIManager.setUpdateUserTable(new IUpdateOnlineUserTable() {
			//添加 或者更新昵称
			@Override
			public void updateOnlineUser(UserInfo userInfo) {
				if(!onlineUsersMap.containsKey(userInfo.getStrIPAddress())){
					synchronized (onlineUsersMap) {
						onlineUsersMap.put(userInfo.getStrIPAddress(), userInfo);
						final String[] row =new String[] {userInfo.getStrHostName(),userInfo.getStrNickName(),userInfo.getStrIPAddress()};
						SwingUtilities.invokeLater(new Runnable()
						{
						      @Override 
						      public void run() 
						      {
						    	  tblModle.addRow(row);
						    	  //更新在线人数
						      }  
						});
					}
					updateOnlineUserNum.updateUserNum(onlineUsersMap.size());
				}
				else
				{
					onlineUsersMap.get(userInfo.getStrIPAddress()).setStrNickName(userInfo.getStrNickName());
					new ChangeName(userInfo).execute();
				}
			}
			
			@Override
			public void removeOnlineUser(String ipAddress) {
				ipAddress = ipAddress.replaceAll("/", "");
				new DeleteUserInfo(ipAddress).execute();
			}
		});
	}
	
	private void initTable(){
		String[] columnNames = {"主机名","昵称","ip"};
		tblModle = new DefaultTableModel();
		
		tblModle.setColumnIdentifiers(columnNames);
		tblOnlineUser = new JTable(tblModle);
		tblOnlineUser.setEnabled(false);
		tblOnlineUser.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){
					int row =((JTable)e.getSource()).rowAtPoint(e.getPoint());
					String ip = tblModle.getValueAt(row, 2).toString();
					UserInfo userInfo = onlineUsersMap.get(ip);
					chatWndManager.openChatWnd(userInfo);
				}
			}
		});
	}
	
	private void  initOnlineUserMap()
	{
		onlineUsersMap = new HashMap<String, UserInfo>();
	}
	
	public JTable getOnlineUserTable()
	{
		return this.tblOnlineUser;
	}
	
//	public int getInfonum()
//	{
//		return tblModle.getRowCount();
//	}
	
	public void setUpdateUserNum(IUpdateOnlineUserNum updateOnlineUserNum){
		this.updateOnlineUserNum = updateOnlineUserNum;
	}
	
	private class DeleteUserInfo extends SwingWorker<Integer , Object>
	{
		String logoutInetAddress;
		DeleteUserInfo(String logoutInetAddress)
		{
			this.logoutInetAddress = logoutInetAddress;
		}
		@Override
		protected Integer doInBackground() throws Exception 
		{
		
			if(!logoutInetAddress.equals(InetAddress.getLocalHost().getHostAddress()))
			{
				int row = tblModle.getRowCount();
				for(int i = 0;i<row;i++)
				{
					if(tblModle.getValueAt(i,2).equals(logoutInetAddress))
					{
						return i;
					}
				}
			}
			return 0;
		}
		@Override
		protected void done() 
		{
		
			int i = 0;
			
			
			try {
				i = get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(i!=0)
			{
				tblModle.removeRow(i);
				synchronized (onlineUsersMap) 
				{
					onlineUsersMap.remove(logoutInetAddress);
				}
				//更新在线人数
			}	
		}
	}
	
	private class ChangeName extends SwingWorker<Integer, Object>{
		
		UserInfo userInfo;
		public ChangeName(UserInfo userInfo) {
			// TODO Auto-generated constructor stub
			this.userInfo =userInfo;
		}
		@Override
		protected Integer doInBackground() throws Exception {
			// TODO Auto-generated method stub
			int row = tblModle.getRowCount();
			for(int i = 0;i<row;i++)
			{
				if(tblModle.getValueAt(i,2).equals(userInfo.getStrIPAddress()))
				{
					return i;
					
				}
			}
			return -1;
		}
		@Override
		protected void done() {
			// TODO Auto-generated method stub
			int i = 0;
			try {
				i = get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(i>=0){
			tblModle.setValueAt(userInfo.getStrNickName(), i, 1);
			}
		}
	}
	
	
	public void delAllInfo()
	{
		int n = tblModle.getRowCount();
		while(n-->0){
			tblModle.removeRow(0);
		}
		onlineUsersMap.clear();
	}
}
