package team.wwg.lansharing.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import javax.swing.JOptionPane;

public class FileReceiverHandle {
	
	
	private FileOutputStream fos = null;
	private FileChannel filechannel = null;
	
	private int BufferSize = 2 * 1024 * 1024;
	
	private boolean fileReadEnd = false;
	private ByteBuffer buffer = ByteBuffer.allocateDirect(BufferSize);

	
	private File targetFile;
	private int TheBufferCount = 0;

	private Selector selector;
	private String fileUir;
	
	public FileReceiverHandle(Selector selector,File targetFile,String fileuir){
		this.selector = selector;
		this.targetFile = targetFile;
		this.fileUir = fileuir;
	}
	
	
	
	public void handleInput(SelectionKey key) throws IOException {
		if (key.isValid()) {
			SocketChannel sc = (SocketChannel) key.channel();
			if (key.isConnectable()) {
				if (sc.finishConnect()) {
					key.interestOps(SelectionKey.OP_READ);
					System.out.println("ClientRecTask.  key.isConnectable()) { ");
					doWrite(sc);
					selector.wakeup();
				} else {
					System.exit(1);// 连接失败，进程退出
				}
			}
			if (key.isReadable()) {
//				System.out.println("ClientRecTask.   key.isReadable() ");
				// 向服务器发信息,信息中即服务器上的文件名
				receiveFile(sc, targetFile, key);
			}

		}
	}
	
	
	private void doWrite(SocketChannel schannel) throws IOException {
		
		// byte[] bytes = "What time is it now?".getBytes();
		// byte[] bytes = fileUir.getBytes("UTF-8");
		byte[] bytes = fileUir.getBytes();
		ByteBuffer buff = ByteBuffer.allocate(bytes.length);
		buff.put(bytes);
		buff.flip();
		schannel.write(buff);
		// 判断是否发送完毕
		if (!buff.hasRemaining()) {
			System.out.println("SEND SUCCESS!\r\n");
		}
	}
	
	
	private void receiveFile(SocketChannel socketchannel, File file, SelectionKey key) {

		try {
			try {

				if (fos == null) {
					fos = new FileOutputStream(file);
				}

				if (filechannel == null) {
					filechannel = fos.getChannel();
					buffer.clear();
				}

				int size = 0;

				while (key.isReadable()) {

					// 你会发现 filechannel 一般情况下是不会出现 读写为 0 的情况
					// 而 socketchannel 因为在非阻塞模式，所以他的每次读写都不能确定是否成功
					// 简单粗暴的做法就是如果 读入 或者写入 为0 的时候 就进行循环，直到返回不为0 为止

					while (true) {
						size =  socketchannel.read(buffer);
						// System.out.println("size =
						// socketChannel.read(buffer);:::"+size);
//						System.out.println("the read size:" + size);

						TheBufferCount += size;
//						System.out.println("TheBufferCount += size;::::" + TheBufferCount);
						if (TheBufferCount >= BufferSize) {
							TheBufferCount = 0;
							break;
						}
						if (size == 0) {
							key.interestOps(SelectionKey.OP_READ);
							key.selector().wakeup();
							return;
						} else if (size < 0) {
							fileReadEnd = true;
							break;
						}
					}

					buffer.flip();
					int tt = filechannel.write(buffer);
//					System.out.println("filechannel.write(buffer)" + tt);

					buffer.compact();

					// System.out.println("the size :"+size);
					if (fileReadEnd) {
						fileReadEnd = false;
						filechannel.close();
						fos.close();
						socketchannel.close();
						key.cancel();
						key.selector().wakeup();
						JOptionPane.showMessageDialog(null, "文件已接收完毕！", "文件信息", JOptionPane.INFORMATION_MESSAGE);

						return;
					}

				}

				// key.interestOps(SelectionKey.OP_READ);
				// key.selector().wakeup();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} 
		finally {
			try {
			//	channel.close();
			} catch (Exception ex) {
			}
			try {
			//	fos.close();
			} catch (Exception ex) {
			}
		}
	}
	
}
