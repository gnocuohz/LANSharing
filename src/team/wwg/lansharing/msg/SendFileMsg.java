package team.wwg.lansharing.msg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import team.wwg.lansharing.user.UserInfo;
import team.wwg.lansharing.util.BaseRequest;
import team.wwg.lansharing.util.BytesUtil;

public class SendFileMsg implements BaseMsg {

	private String fileName;
	private UserInfo info;
	private long filelength;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public UserInfo getInfo() {
		return info;
	}
	public void setInfo(UserInfo info) {
		this.info = info;
	}
	public long getFilelength() {
		return filelength;
	}
	public void setFilelength(long filelength) {
		this.filelength = filelength;
	}
	
	public SendFileMsg(byte[] message){
		formBytes(message);
	}
	
	/**
	 * 
	 * @param fileName 文件名
	 * @param info  用户信息
	 * @param filelength  文件长度
	 */
	
	public SendFileMsg(String fileName, UserInfo info, long filelength) {
		super();
		this.fileName = fileName;
		this.info = info;
		this.filelength = filelength;
	}
	
	public byte[] toBytes(){
		
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

		byte[] bytemsgType = null;
		
		byte[] byteuserInfo = null;
		byte[] byteuserInfoLenth = null;
		
		
		byte[] bytefileName = null;
		byte[] bytefileNamelen = null;
		byte[] bytefilelenth = null;
		
		byte[] byteMainmsg = null;
		byte[] byteMainmsglenth = null;
		int intMainmsglenth = 0;
		try {
			
			
			byteuserInfo = info.toBytes();
			byteuserInfoLenth = BytesUtil.intToByteArray(byteuserInfo.length);
			
			
			bytefileName = this.fileName.getBytes("UTF-8");
			bytefileNamelen = BytesUtil.intToByteArray(bytefileName.length);
			
			
			bytefilelenth = BytesUtil.longToByte(filelength);
			bytemsgType = BytesUtil.intToByteArray(BaseRequest.MSG_TYPE_SEND_FILE);
			
			// 获取长度计算
//			byteIPAdress.length+/
			intMainmsglenth =byteuserInfoLenth.length+ byteuserInfo.length + bytefileName.length + bytefileNamelen.length+bytefilelenth.length;
			
			byteMainmsglenth = BytesUtil.intToByteArray(intMainmsglenth);
			
			// 写入流
			arrayOutputStream.write(bytemsgType);
			arrayOutputStream.write(byteMainmsglenth);
			
			arrayOutputStream.write(byteuserInfoLenth);
			arrayOutputStream.write(byteuserInfo);
			
			arrayOutputStream.write(bytefileNamelen);
			arrayOutputStream.write(bytefileName);
			
			arrayOutputStream.write(bytefilelenth);

			
			byteMainmsg = arrayOutputStream.toByteArray();
			arrayOutputStream.flush();
			arrayOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteMainmsg;
		
	}
	
	
	public void formBytes(byte[] data){
		
		ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
		
		
		byte[] byte_msgType = new byte[4];
		byte[] byteMsgLenth = new byte[4];
		byte[] byteUserinfoLenth = new byte[4];
		byte[] bytefileNameLenth = new byte[4];
		byte[] bytefileLenth = new byte[8];
		
		
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
		
		
		arrayInputStream.read(bytefileNameLenth, 0, 4);
		int fileNamelenth = BytesUtil.byteArrayToInt(bytefileNameLenth);
		byte[] bytefileName =  new byte[fileNamelenth];
		arrayInputStream.read(bytefileName, 0, fileNamelenth);
		try {
			this.fileName = new String(bytefileName,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		arrayInputStream.read(bytefileLenth, 0, 8);
		this.filelength = BytesUtil.byteToLong(bytefileLenth);
		
		try {
			arrayInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
}
