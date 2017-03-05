package org.pesho.judge.run;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import static org.junit.Assume.assumeFalse;
import org.junit.Before;
import org.junit.Test;

public class CommanRunnerTest {

	@Before
	public void beforeMethod() {
		assumeFalse(System.getProperty("os.name").toLowerCase().contains("win"));
	}

	@Test
	public void testEcho() throws Exception {
		CommandRunner runner = new CommandRunner("echo", new String[] { "test" }, 5000);
		runner.start();
		int exitCode = runner.waitFor();
		assertThat(exitCode, is(0));
		assertThat(runner.getOutput(), is("test\n"));
	}

	@Test
	public void testEchoRun() throws Exception {
		CommandRunner runner = new CommandRunner("echo", new String[] { "test" }, 5000);
		int exitCode = runner.run();
		assertThat(exitCode, is(0));
		assertThat(runner.getOutput(), is("test\n"));
	}

	@Test
	public void testParallelEcho() throws Exception {
		Thread[] t = new Thread[100];
		for (int i = 0; i < t.length; i++) {
			t[i] = new Thread() {
				@Override
				public void run() {
					try {
						CommandRunner runner = new CommandRunner("echo", new String[] { "test" }, 5000);
						runner.start();
						int exitCode = runner.waitFor();
						assertThat(exitCode, is(0));
						assertThat(runner.getOutput(), is("test\n"));
					} catch (Exception e) {
						e.printStackTrace();
						fail("error occurred: " + e.getMessage());
					}
				}
			};
		}
		for (int i = 0; i < t.length; i++) {
			t[i].start();
		}
	}

	@Test
	public void testSleep() throws Exception {
		long startTime = System.currentTimeMillis();
		CommandRunner runner = new CommandRunner("sleep", new String[] { "1" }, 5000);
		runner.start();
		int exitCode = runner.waitFor();
		long totalTime = System.currentTimeMillis() - startTime;
		assertThat(exitCode, is(0));
		assertThat(totalTime, is(greaterThan(1000L)));
	}

	@Test
	public void testKill() throws Exception {
		long startTime = System.currentTimeMillis();
		CommandRunner runner = new CommandRunner("sleep", new String[] { "2" }, 5000);
		runner.start();
		runner.kill();
		int exitCode = runner.waitFor();
		long totalTime = System.currentTimeMillis() - startTime;
		assertThat(exitCode, not(0));
		assertThat(totalTime, is(lessThan(2000L)));
	}

	@Test
	public void testTimeout() throws Exception {
		long startTime = System.currentTimeMillis();
		CommandRunner runner = new CommandRunner("sleep", new String[] { "3" }, 1000);
		runner.start();
		int exitCode = runner.waitFor();
		long totalTime = System.currentTimeMillis() - startTime;
		assertThat(exitCode, not(0));
		assertThat(totalTime, is(lessThan(2000L)));
	}

}
