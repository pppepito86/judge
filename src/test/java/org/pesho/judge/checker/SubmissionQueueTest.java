package org.pesho.judge.checker;

import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeThat;

import org.junit.Test;

public class SubmissionQueueTest {
	
	@Test
	public void test() throws Exception {
		SubmissionQueue queue = SubmissionQueue.getInstance();
		queue.put(3);
		assumeThat(queue.take(), is(3));
	}

}
