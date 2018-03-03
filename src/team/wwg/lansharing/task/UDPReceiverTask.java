package team.wwg.lansharing.task;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import team.wwg.lansharing.msg.MsgParserHandler;
import team.wwg.lansharing.util.BaseRequest;

public class UDPReceiverTask extends UDPChannelTask {
	
	private MsgParserHandler msgParserHandler = null;
	
	public UDPReceiverTask(Selector selector) {
		super(selector);
		initUDPReceiverChannel();
	}
	
	private void initUDPReceiverChannel(){
		this.bindReceivePort(BaseRequest.UDP_PORT);
		this.allocateBuffer(1024);
		msgParserHandler = new MsgParserHandler(selector);
	}
	
	public void register(){
		try {
			this.key = this.selectableChannel.register(this.selector,SelectionKey.OP_READ);
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void run() {
		byteBuffer.clear();
		try {
			((DatagramChannel) selectableChannel).receive(byteBuffer);
			msgParserHandler.parsingMsg(byteBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		key.interestOps(SelectionKey.OP_READ);
		selector.wakeup();
	}
}
