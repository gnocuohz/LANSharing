package team.wwg.lansharing.task;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import team.wwg.lansharing.msg.MsgParserHandler;
import team.wwg.lansharing.util.BaseRequest;


public class MulticastReceiver extends ChannelTask {
	private final static String ALL_SYSTEMS_MCAST_NET = "224.0.0.2";
	private MsgParserHandler msgParserHandler = null;
	private DatagramChannel multicastChannel = null;
	
	
	public MulticastReceiver(Selector selector) {
		super(selector);
		initMulticastChannel();
	}
	
	private void initMulticastChannel() {
		msgParserHandler = new MsgParserHandler();
		this.allocateBuffer(64);
		InetAddress localAddress = null;
		try {
			localAddress = InetAddress.getLocalHost();
			NetworkInterface ni = NetworkInterface.getByInetAddress(localAddress);
			multicastChannel = DatagramChannel.open(StandardProtocolFamily.INET)
					.setOption(StandardSocketOptions.SO_REUSEADDR, true)
					.bind(new InetSocketAddress(BaseRequest.MULTICAST_PORT))
					.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
			multicastChannel.configureBlocking(false);
			InetAddress group = InetAddress.getByName(ALL_SYSTEMS_MCAST_NET);
			multicastChannel.join(group, ni);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	public SelectableChannel getChannel() {
		return multicastChannel;
	}
	
	public void register(){
		try {
			this.key = multicastChannel.register(selector,SelectionKey.OP_READ);
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void run() {
		byteBuffer.clear();
		try {
			multicastChannel.receive(byteBuffer);
			msgParserHandler.parsingMsg(byteBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		key.interestOps(SelectionKey.OP_READ);
		selector.wakeup();
	}
}
