package team.wwg.lansharing.user;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import team.wwg.lansharing.util.BytesUtil;



public class UserInfo {
	
	private String strHostName = null;
	private String strNickName = null;
	private String strIPAddress = null;
	
	public UserInfo(String strHostName,String strNickName,String strIPAddress){
		this.strHostName = strHostName;
		this.strNickName = strNickName;
		this.strIPAddress = strIPAddress.replaceAll("/", "");
	}
	
	public String getStrHostName() {
		return strHostName;
	}
	public void setStrHostName(String strHostName) {
		this.strHostName = strHostName;
	}
	
	public String getStrNickName() {
		return strNickName;
	}
	public void setStrNickName(String strNickName) {
		this.strNickName = strNickName;
	}
	
	public String getStrIPAddress() {
		return strIPAddress;
	}
	public void setStrIPAddress(String strIPAddress) {
		strIPAddress = strIPAddress.replaceAll("/", "");
		this.strIPAddress = strIPAddress;
	}
	
	
	public byte[] toBytes(){
		
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		
		byte[] byteHostName = null;
		byte[] byteNickname = null;
		byte[] byteIpAddress = null;
		byte[] byteMsg = null;
	
		try {
			
			byteNickname = this.strNickName.getBytes("UTF-8");
			byteIpAddress = this.strIPAddress.getBytes("UTF-8");
			byteHostName = this.strHostName.getBytes("UTF-8");
	
			byte[] nicknameLenth = BytesUtil.intToByteArray(byteNickname.length);
			byte[] IPAdressLenth = BytesUtil.intToByteArray(byteIpAddress.length);
			byte[] hostNameLenth = BytesUtil.intToByteArray(byteHostName.length);
			

			// 获取长度计算
		

			// 写入流
			arrayOutputStream.write(hostNameLenth);
			arrayOutputStream.write(byteHostName);
			
			arrayOutputStream.write(IPAdressLenth);
			arrayOutputStream.write(byteIpAddress);
			
			arrayOutputStream.write(nicknameLenth);
			arrayOutputStream.write(byteNickname);

			byteMsg = arrayOutputStream.toByteArray();
			
			arrayOutputStream.flush();
			arrayOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteMsg;
	}
	
	
	public void fromBytes(byte[] data){
		
		ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
		
		byte[] byteHostNameLenth = new byte[4];
		byte[] byteIPAdressLenth = new byte[4];
		byte[] bytenicknameLenth = new byte[4];
		
		
		
		byte[] byteHostName = null;
		byte[] byteNickname = null;
		byte[] byteIpAddress = null;
		
		
		arrayInputStream.read(byteHostNameLenth, 0, 4);
		
		int hostNamlenth = BytesUtil.byteArrayToInt(byteHostNameLenth);
		byteHostName = new byte[hostNamlenth];
		arrayInputStream.read(byteHostName, 0, hostNamlenth);
		
		
		
		arrayInputStream.read(byteIPAdressLenth, 0, 4);
		
		int IPAddressLenth = BytesUtil.byteArrayToInt(byteIPAdressLenth);
		byteIpAddress = new byte[IPAddressLenth];
		arrayInputStream.read(byteIpAddress, 0, IPAddressLenth);
		
		
		
		arrayInputStream.read(bytenicknameLenth, 0, 4);
		int nicknameLenth = BytesUtil.byteArrayToInt(bytenicknameLenth);
		byteNickname = new byte[nicknameLenth];
		arrayInputStream.read(byteNickname, 0, nicknameLenth);
		
		try {
			this.strHostName = new String(byteHostName, "UTF-8");
			this.strIPAddress = new String(byteIpAddress, "UTF-8");
			this.strNickName = new String(byteNickname, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
}
