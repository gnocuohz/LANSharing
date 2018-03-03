package team.wwg.lansharing.task;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import team.wwg.lansharing.util.BaseRequest;
import team.wwg.lansharing.util.FileUtil;

public class TCPFileReceiverTask extends TCPChannelTask {

	private String host;
	private int port = BaseRequest.SOCKET_PORT;
	private String fileUir;
	private File targetFile;

	
	/**
	 * 
	 * @param host
	 * 对方的主机名
	 * @param selector
	 * 	 selector
	 * @param fileUir
	 * 对方的文件uir
	 */
	public TCPFileReceiverTask(String host, Selector selector, String fileUir) {
		super(selector);
		this.host = host == null ? "127.0.0.1" : host;
		this.fileUir = fileUir;
		this.targetFile = FileUtil.getTargetFile(fileUir);

		init();
	}
	
	
	/**
	 * 
	 * @param host
	 *            对方的主机名
	 * @param selector
	 *            selector
	 * @param fileUir
	 *            对方的文件uir
	 * @param newPath
	 *            自己要保存的本地新路径
	 */
	public TCPFileReceiverTask(String host, Selector selector, String fileUir, String newPath){
		super(selector);
		this.host = host == null ? "127.0.0.1" : host;
		this.fileUir = fileUir;
		this.targetFile = FileUtil.getTargetFile(fileUir, newPath);
	
		init();
	}
	
	
	

	private void init() {
		try {
			this.selectableChannel = SocketChannel.open();
			this.selectableChannel.configureBlocking(false);
			SocketAddress socketAddress = new InetSocketAddress(host, port);
			((SocketChannel) this.selectableChannel).connect(socketAddress);

		} catch (IOException e) {

			e.printStackTrace();
		}
		System.out.println("create TCPFileReceiverTask !!!!!!!!!!!");
	}
	
	public void register() {
		try {
//			if (channel.isConnected()) {
//				this.key = channel.register(selector, SelectionKey.OP_READ);
//				//Handle handle =new Handle();
//				//handle.doWrite(channel);
//				selector.wakeup();
//			} else {
				this.key = this.selectableChannel.register(selector, SelectionKey.OP_CONNECT);
				this.key.attach(new FileReceiverHandle(selector,targetFile,fileUir));
				selector.wakeup();
		//	}
			//this.key.attach(this);
			System.out.println("TCPFileReceiverTask.   register ");
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		} 
	};
	@Override
	public void run() {
		try{
			final FileReceiverHandle handle = (FileReceiverHandle)key.attachment();
			if (handle != null)
				handle.handleInput(key);
			
		}catch (IOException e) {

			e.printStackTrace();
		}
	}

}