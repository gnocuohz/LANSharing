package team.wwg.lansharing.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import team.wwg.lansharing.ui.MainWnd;
import team.wwg.lansharing.user.UserInfo;


public class LocalInfoUtil {
	public static UserInfo getSelfInfo()
	{
		String strHostName = null;
		String strIPAddress = null;
		try {
			strHostName = InetAddress.getLocalHost().getHostName();
			strIPAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new UserInfo(strHostName,MainWnd.nickName.getText(),strIPAddress);
	}
}
