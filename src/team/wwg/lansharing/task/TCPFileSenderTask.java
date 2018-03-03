package team.wwg.lansharing.task;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


public class TCPFileSenderTask extends TCPChannelTask{

	public TCPFileSenderTask(Selector selector , SocketChannel clientChannel) {
		super(selector);
		this.selectableChannel = clientChannel;
		init();
	}

	private void init(){
		System.out.println("create TCPFileSenderTask !!!!!!!!!!!");
	}
	
	
	public void register() {
		try {
			
			key = this.selectableChannel.register(selector, SelectionKey.OP_READ);
			key.attach(new FileSenderHandle(selector));
			System.out.println("clientTask register selector!!!!!!!!!!!");
			//doWrite(clientChannel);
			
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void run() {
		
		if (key.isReadable() || key.isWritable()) {
			
			final FileSenderHandle handle = (FileSenderHandle) key.attachment();
			if (handle != null)
				handle.handle(key);
		}
	}
}
