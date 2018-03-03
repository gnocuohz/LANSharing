package team.wwg.lansharing.msg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import team.wwg.lansharing.user.UserInfo;
import team.wwg.lansharing.util.BaseRequest;
import team.wwg.lansharing.util.BytesUtil;

public class LoginMsg implements BaseMsg{

	private UserInfo info;

	public UserInfo getInfo() {
		return info;
	}

	public void setInfo(UserInfo info) {
		this.info = info;
	}

	public LoginMsg(UserInfo info) {
	
		this.info = info;
	}

	public LoginMsg(byte[] message) {
		
		formBytes(message);
	}
	
	public byte[] toBytes() {
		
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

		byte[] byte_msgType = null;
		byte[] byteUserinfo = null;

		byte[] byteUserinfolenth = null;
		
		byte[] byteMainMsg = null;
		byte[] byteMainMsgLenth = null;
		int MainmsgLenth = 0;

		try {

			byte_msgType = BytesUtil.intToByteArray(BaseRequest.MSG_TYPE_LOGIN);

			byteUserinfo = this.info.toBytes();
			
			byteUserinfolenth = BytesUtil.intToByteArray(byteUserinfo.length);
			// 获取长度计算
			// byteIPAdress.length+

			MainmsgLenth = byteUserinfo.length+byteUserinfolenth.length;

			byteMainMsgLenth = BytesUtil.intToByteArray(MainmsgLenth);

			// 写入流
			arrayOutputStream.write(byte_msgType);
			arrayOutputStream.write(byteMainMsgLenth);
			arrayOutputStream.write(byteUserinfolenth);
			arrayOutputStream.write(byteUserinfo);

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
	
		arrayInputStream.read(byte_msgType, 0, 4);
		arrayInputStream.read(byteMsgLenth, 0, 4);
		arrayInputStream.read(byteUserinfoLenth, 0, 4);
		
		int main_msg_len = BytesUtil.byteArrayToInt(byteMsgLenth);
		int _msgType = BytesUtil.byteArrayToInt(byte_msgType);
		
		int userLenth = BytesUtil.byteArrayToInt(byteUserinfoLenth);	
		
		byte[] byteUserinfo =  new byte[userLenth];
		
		arrayInputStream.read(byteUserinfo, 0, userLenth);
		info = new UserInfo("", "", "");
		info.fromBytes(byteUserinfo);
		try {
			arrayInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
}
