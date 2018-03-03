package team.wwg.lansharing.task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;

public class UDPChannelTask extends ChannelTask {
	
	public UDPChannelTask(Selector selector) {
		super(selector);
		initChannel();
	}
	
	private void initChannel(){
		try {
			this.selectableChannel = DatagramChannel.open();
			this.selectableChannel.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void bindReceivePort(int port){
		try {
			((DatagramChannel) this.selectableChannel).socket().bind(new InetSocketAddress(port));
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
