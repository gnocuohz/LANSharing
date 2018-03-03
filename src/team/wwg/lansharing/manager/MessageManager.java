package team.wwg.lansharing.manager;

import java.nio.channels.SelectionKey;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import team.wwg.lansharing.msg.WaitingSendMsg;

public class MessageManager {

	private static MessageManager messageManager = null;
	
	private Queue<WaitingSendMsg> waitSendMsgQueue = null;
	
	private SelectionKey key = null;
	
	private MessageManager() {
		waitSendMsgQueue = new ConcurrentLinkedQueue<WaitingSendMsg>();
	}
	
	public synchronized static MessageManager getInstance() {
		if (null == messageManager) {
			messageManager = new MessageManager();
		}
		return messageManager;
	}
	
	public void setKey(SelectionKey key){
		this.key = key;
	}
	
	public Queue<WaitingSendMsg> getWaitSendMsgQueue(){
		return waitSendMsgQueue;
	}
	
	public void addWaitSendMsg(WaitingSendMsg msg) {
		synchronized (waitSendMsgQueue) {
			if (waitSendMsgQueue.contains(msg)) {
				// “‘±ªÃÌº”
				return;
			}
			waitSendMsgQueue.add(msg);
		}
		if(key != null){
			key.interestOps(SelectionKey.OP_WRITE);
			key.selector().wakeup();
		}
	}
	
	public void removeWaitSendMsg(WaitingSendMsg msg) {
		synchronized (waitSendMsgQueue) {
			if (waitSendMsgQueue.contains(msg)) {
				waitSendMsgQueue.remove(msg);
			}
		}
	}
}
