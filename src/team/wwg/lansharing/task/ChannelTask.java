package team.wwg.lansharing.task;

import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;

import team.wwg.lansharing.manager.TaskManager;

public class ChannelTask extends BaseTask{
	
	protected TaskManager taskManager = null;
	protected ByteBuffer byteBuffer = null;
	protected SelectableChannel selectableChannel = null;
	
	public ChannelTask(Selector selector) {
		super(selector);
		this.taskManager = TaskManager.getInstance();
		taskManager.addWaitTask(this);
	}
	
	public void register(){
//		try {
//			selectableChannel.register(this.getSelector(), ops);
//		} catch (ClosedChannelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	protected void allocateBuffer(int capacity){
		byteBuffer = ByteBuffer.allocate(capacity);
	}
	
	public SelectableChannel getChannel() {
		return selectableChannel;
	}
	
}
