package team.wwg.lansharing.task;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class TCPFileServerTask extends TCPChannelTask {

	public TCPFileServerTask(Selector selector) {
		super(selector);
		init();
	}


	private void init() {
		try {
			this.selectableChannel = ServerSocketChannel.open();
			selectableChannel.configureBlocking(false);
			ServerSocket serverSocket = ((ServerSocketChannel) this.selectableChannel).socket();
			if (serverSocket != null) {
				serverSocket.bind(this.inetSocketAddress);
			}
			System.out.println("TCPFileServerTask create!!!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void register() {
		try {
			serverKey = this.selectableChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("TCPFileServerTask.register!!!!!!!");
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	@Override
	public synchronized void run() {
		try {
			int count = 0;
			do {
				System.out.println("TCPFileServerTask.running !!!!!!!!!!!!");
				ServerSocketChannel serverChannel = (ServerSocketChannel) this.serverKey.channel();
				SocketChannel socketChannel = serverChannel.accept();
				if (socketChannel == null)
					break;
				socketChannel.configureBlocking(false);

				System.out.println("new client  connected");
				
				TCPFileSenderTask fileSenderTask = new TCPFileSenderTask(selector, socketChannel);
				
				this.selector.wakeup();
				
			} while (++count < 21);
			serverKey.interestOps(SelectionKey.OP_ACCEPT);
			// taskManager.addWaitingTask(ServerTask.this);// 添加到任务管理
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
