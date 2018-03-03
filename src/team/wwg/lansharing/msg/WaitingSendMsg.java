package team.wwg.lansharing.msg;

/**
 * 存放需要发送消息的对象
 *
 */

public class WaitingSendMsg {
	
	private byte[] message = null;
	private String IPAddress = null;
	private int port = 0;
	public WaitingSendMsg(byte[] message,String IPAddress,int port) {
		this.message = message;
		this.IPAddress = IPAddress;
		this.port = port;
	}
	public byte[] getMessage() {
		return message;
	}
	public void setMessage(byte[] message) {
		this.message = message;
	}
	public String getIPAddress() {
		return IPAddress;
	}
	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
