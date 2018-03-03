package team.wwg.lansharing.manager;

import team.wwg.lansharing.service.IUpdateOnlineUserTable;
import team.wwg.lansharing.user.UserInfo;

public class MainWndManager {
	private static MainWndManager updateUIManager = null;
	
	private IUpdateOnlineUserTable updateOnlineUserTable = null;
	
	public synchronized static MainWndManager getInstance() {
		if (null == updateUIManager) {
			updateUIManager = new MainWndManager();
		}
		return updateUIManager;
	}
	private MainWndManager() {
		
	}

	/**
	 * @param updateOnlineUserTable  »Øµ÷£¬£¬
	 */
	public void setUpdateUserTable(IUpdateOnlineUserTable updateOnlineUserTable){
		this.updateOnlineUserTable = updateOnlineUserTable;
	}
	
	public void invokingUpdateOnlineUser(UserInfo userInfo){
		if(updateOnlineUserTable != null){
			updateOnlineUserTable.updateOnlineUser(userInfo);
		}
	}
	
	public void invokingRemoveOnlineUser(String ipAddress){
		if(updateOnlineUserTable != null){
			updateOnlineUserTable.removeOnlineUser(ipAddress);
		}
	}
}
