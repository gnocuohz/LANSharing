package team.wwg.lansharing.task;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import team.wwg.lansharing.util.BaseRequest;

public class TCPChannelTask extends ChannelTask{

	protected SelectionKey serverKey;
	protected InetSocketAddress inetSocketAddress;
	public TCPChannelTask(Selector selector) {
		super(selector);
		init();
	}
	
	private void init(){
		inetSocketAddress = new InetSocketAddress(BaseRequest.SOCKET_PORT);
		System.out.println("ChannelTask create!!!");
	}
	
}
