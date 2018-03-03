package team.wwg.lansharing.msg;

public interface BaseMsg {
	public byte[] toBytes();
	public void formBytes(byte[] data);
}
