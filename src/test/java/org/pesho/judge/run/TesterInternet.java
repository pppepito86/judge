package org.pesho.judge.run;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeThat;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TesterInternet {

	private File sourceFile;
	private File compiledFile;
	private File problemInputFile;
	private File testInputFile;
	private File testOutputFile;
	private File testErrorFile;

	@Before
	public void before() throws Exception {
		assumeFalse(System.getProperty("os.name").toLowerCase().contains("win"));
		int exitCode = new CommandRunner("bash", new String[] { "-c", "which docker" }, 5000).run();
		assumeThat(exitCode, is(0));
		sourceFile = new File("src/test/resources/docker/submission_internet/Internet.java");
		compiledFile = new File("src/test/resources/docker/submission_internet/Internet.class");
		assumeFalse(compiledFile.exists());
		
		problemInputFile = new File("src/test/resources/docker/problem/input1");
		testInputFile = new File("src/test/resources/docker/submission_internet/input1");
		testOutputFile = new File("src/test/resources/docker/submission_internet/output1");
		testErrorFile = new File("src/test/resources/docker/submission_internet/error1");

		assertThat(
				new CommandRunner("cp",
						new String[] { problemInputFile.getAbsolutePath(), testInputFile.getAbsolutePath() }).run(),
				is(0));
	}

	@Test
	public void testTester() throws Exception {
		assertThat(new CompileRunner(sourceFile).run(), is(0));
		
		TestRunner testRunner = new TestRunner(compiledFile, 1, 2000, 64);
		assertThat(testRunner.run(), not(0));
		assertThat(testRunner.isTimedOut(), is(false));
	}

	@After
	public void after() {
		compiledFile.delete();
		testInputFile.delete();
		testOutputFile.delete();
		testErrorFile.delete();
	}

}
