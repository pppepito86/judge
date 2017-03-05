package org.pesho.judge.run;

import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

public class DockerTest {
	
	@Before
	public void beforeMethod() throws Exception {
		assumeFalse(System.getProperty("os.name").toLowerCase().contains("win"));
		int exitCode = new CommandRunner("/bin/bash", new String[] {"-c", "which docker" }, 5000).run();
		assumeThat(exitCode, is(0));
	}
	
	@Test
	public void test() throws Exception {
		DockerRunner dockerRunner = new DockerRunner("echo hello", 50000);
		int exitCode = dockerRunner.run();
		assertThat(exitCode, is(0));
		assertThat(dockerRunner.getOutput(), is("hello\n"));
	}

}
