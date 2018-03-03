package team.wwg.lansharing.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;

import team.wwg.lansharing.util.FileUtil;

public class FileSenderHandle {

	private Selector selector;
	private StringBuilder message;
	int BufferSize = 5 * 1024 * 1024;
	private boolean writeOK = true;
	private ByteBuffer byteBuffer = ByteBuffer.allocate(BufferSize);
	private FileChannel fileChannel;
	private String fileName;
	private boolean fileReadEnd = false;
	private int TheBufferCount = 0;

	public FileSenderHandle(Selector selector) {
		this.selector = selector;

	}

	private void doRread(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		if (writeOK)
			message = new StringBuilder();
		while (true) {
			byteBuffer.clear();
			int r;
			try {
				r = socketChannel.read(byteBuffer);
				if (r == 0)
					break;
				if (r == -1) {
					socketChannel.close();
					key.cancel();
					return;
				}
				message.append(new String(byteBuffer.array(), 0, r));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 将接收到的信息转化成文件名,以映射到服务器上的指定文件
		if (writeOK && invokeMessage(message)) {
			SelectionKey map = key.interestOps(SelectionKey.OP_WRITE);
			map.attach(this);
			selector.wakeup();
			writeOK = false;
		}
	}

	private void doWrite(SelectionKey key) {
		try {

			if (!key.isValid())
				return;
			SocketChannel socketChannel = (SocketChannel) key.channel();
			if (fileChannel == null) {
				fileChannel = new FileInputStream(fileName).getChannel();
				byteBuffer.clear();
			}

			while (key.isWritable()) {

				// 因为 filechannel 是阻塞形式的，所以一般情况下，不会出现 读取和写入为0 的情况

				while (true) {

					int w = fileChannel.read(byteBuffer);

					// 如果文件已写完,则关掉key和socket
					if (w < 0) {
						if (TheBufferCount == 0) {
							fileReadEnd = true;
						}
						break;
					}
					TheBufferCount += w;
					if (TheBufferCount >= BufferSize) {
						TheBufferCount = 0;
						break;
					}

				}

				byteBuffer.flip();
				// 对于不是阻塞的模式来说，每一次的写入都不一定成功，也就是说，每次的写入可能随时会有0的情况
				// 所以在这里进行判断，如果返回的是零，怎重新写入，直到成功写入为止
				// 你会发现 filechannel 一般情况下是不会出现 读写为 0 的情况
				// 而 socketchannel 因为在非阻塞模式，所以他的每次读写都不能确定是否成功
				// 简单粗暴的做法就是如果 读入 或者写入 为0 的时候 就进行循环，直到返回不为0 为止

				TheBufferCount = byteBuffer.limit();
				while (TheBufferCount != 0) {

					int zz = socketChannel.write(byteBuffer);
					// while(zz<=0){
					// zz = socketChannel.write(byteBuffer);
					// }
					if (zz == 0) {
					
						key.interestOps(SelectionKey.OP_WRITE);
						byteBuffer.compact();
						selector.wakeup();
						return;
					}

					byteBuffer.compact();
					byteBuffer.flip();
					TheBufferCount = byteBuffer.limit();
				}
				byteBuffer.compact();

				if (fileReadEnd) {
					fileReadEnd = true;
					fileName = null;
					fileChannel.close();
					fileChannel = null;
					writeOK = true;
					socketChannel.close();
					key.cancel();
					JOptionPane.showMessageDialog(null, "文件发送成功完毕！", "文件信息", JOptionPane.INFORMATION_MESSAGE);
					selector.wakeup();
					return;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		key.interestOps(SelectionKey.OP_WRITE);
		selector.wakeup();
	}

	
	
	public void handle(SelectionKey key) {
		if (key.isReadable()) {
			doRread(key);
		}
		// 向客户端写数据
		if (key.isWritable()) {
			doWrite(key);
		}
	}

	
	
	private boolean invokeMessage(StringBuilder builder) {
		String m = message.toString();

		m = FileUtil.getRightUri(m);

		try {
			File f = new File(m);
			if (!f.exists())
				return false;
			fileName = m;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
