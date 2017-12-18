package org.pesho.judge.queue;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SubmissionsQueue {
	
	protected Queue<Integer> queue = new ConcurrentLinkedQueue<>();
	
	public void add(int submissionId) {
		queue.add(submissionId);
	}
	
	public Integer peek() {
		return queue.peek();
	}
	
	public Integer poll() {
		return queue.poll();
	}
	
	public Iterator<Integer> iterator() {
		return queue.iterator();
	}

}
