package org.pesho.judge.checker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SubmissionQueue {
	
	private static final SubmissionQueue INSTANCE = new SubmissionQueue();
	
	public static SubmissionQueue getInstance() {
		return INSTANCE;
	}
	
	private BlockingQueue<Integer> submissions = new LinkedBlockingQueue<Integer>();

	public void put(int id) throws InterruptedException {
		submissions.put(id);
	}
	
	public int take() throws InterruptedException {
		return submissions.take();
	}

}
