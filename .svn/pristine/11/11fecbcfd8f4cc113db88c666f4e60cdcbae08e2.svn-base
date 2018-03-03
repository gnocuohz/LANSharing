package team.wwg.lansharing.task;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public class BaseTask extends Thread{
	protected Selector selector; // —°‘Òπ‹¿Ì∆˜
	protected SelectionKey key = null;
	
	BaseTask(Selector selector) {
		this.selector = selector;
	}

	public BaseTask() {
	}

	@Override
	public void run() {
		System.out.println("BaseTask");
	}

	public Selector getSelector() {
		return selector;
	}

	
	public void setSelector(Selector selector) {
		this.selector = selector;
	}
}
