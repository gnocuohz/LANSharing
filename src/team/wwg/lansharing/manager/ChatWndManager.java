package team.wwg.lansharing.manager;

import java.util.HashMap;

import javax.swing.SwingUtilities;

import team.wwg.lansharing.msg.ChatMsg;
import team.wwg.lansharing.msg.SendFileMsg;
import team.wwg.lansharing.ui.ChatWnd;
import team.wwg.lansharing.user.UserInfo;

public class ChatWndManager {
	
	private static ChatWndManager chatWndManager = null;
	
	private static HashMap<String, ChatWnd> chatWndMap = null;
	
	private ChatWndManager() {
		chatWndMap = new HashMap<String, ChatWnd>();
	}
	
	public synchronized static ChatWndManager getInstance() {
		if (null == chatWndManager) {
			chatWndManager = new ChatWndManager();
		}
		return chatWndManager;
	}
	
	public synchronized void addChatWnd(String strIPAddress, ChatWnd chatWnd) {
		if (containsChatWndMap(strIPAddress)) {
			// 被添加就返回
			return;
		}
		chatWndMap.put(strIPAddress, chatWnd);
	}
	
	public synchronized void removeChatWnd(String strIPAddress) {
		synchronized (chatWndMap) {
			if (chatWndMap.containsKey(strIPAddress)) {
				chatWndMap.remove(strIPAddress);
				}
			}
	}
	
	public synchronized ChatWnd getChatWnd(String strIPAddress) {
		synchronized (chatWndMap) {
			if (chatWndMap.containsKey(strIPAddress)) {
				return chatWndMap.get(strIPAddress);
				}
			}
		return null;
	}

	public synchronized boolean containsChatWndMap(String strIPAddress) {
		synchronized (chatWndMap) {
			if (chatWndMap.containsKey(strIPAddress)) {
				return true;
				}else
					return false;
			}
	}
	
	public void updateChatWndMsgArea(ChatMsg chatMsg){
		String ip = chatMsg.getInfo().getStrIPAddress();
		if(containsChatWndMap(ip)){
			getChatWnd(ip).updateMessage(chatMsg);
		}else{
			beOpenedChatWnd(chatMsg);
		}
	}
	
	public void updateChatWndFileMsg(SendFileMsg sendFileMsg){
		String ip = sendFileMsg.getInfo().getStrIPAddress();
		if(containsChatWndMap(ip)){
			getChatWnd(ip).updateFileMessage(sendFileMsg);
		}else{
			updateChatWndFileMsg(sendFileMsg);
		}
	}
	
	public void beOpenedChatWnd(ChatMsg chatMsg){
			SwingUtilities.invokeLater(new Runnable() 
			{  
			      @Override 
			      public void run() 
			      {
			    	  ChatWnd chatWnd = new ChatWnd(chatMsg.getInfo());
			    	  chatWnd.updateMessage(chatMsg);
			    	  addChatWnd(chatMsg.getInfo().getStrIPAddress(), chatWnd);
			      }  
			});  	
	}
	
	public void beOpenedChatFileWnd(SendFileMsg sendFileMsg){
		SwingUtilities.invokeLater(new Runnable() 
		{  
		      @Override 
		      public void run() 
		      {
		    	  ChatWnd chatWnd = new ChatWnd(sendFileMsg.getInfo());
		    	  chatWnd.updateFileMessage(sendFileMsg);
		    	  addChatWnd(sendFileMsg.getInfo().getStrIPAddress(), chatWnd);
		      }  
		});  	
}
	
	public void openChatWnd(UserInfo userInfo){
		if(!containsChatWndMap(userInfo.getStrIPAddress())){
			SwingUtilities.invokeLater(new Runnable() 
			{  
			      @Override 
			      public void run() 
			      {
			    	  ChatWnd chatWnd = new ChatWnd(userInfo);
			    	  addChatWnd(userInfo.getStrIPAddress(), chatWnd);
			      }  
			});  
		}	
	}
}
