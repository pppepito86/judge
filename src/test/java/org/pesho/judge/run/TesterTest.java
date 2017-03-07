package org.pesho.judge.run;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeThat;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TesterTest {

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
		sourceFile = new File("src/test/resources/docker/submission/Sum.java");
		compiledFile = new File("src/test/resources/docker/submission/Sum.class");
		assumeFalse(compiledFile.exists());
		
		problemInputFile = new File("src/test/resources/docker/problem/input1");
		testInputFile = new File("src/test/resources/docker/submission/input1");
		testOutputFile = new File("src/test/resources/docker/submission/output1");
		testErrorFile = new File("src/test/resources/docker/submission/error1");

		assertThat(
				new CommandRunner("cp",
						new String[] { problemInputFile.getAbsolutePath(), testInputFile.getAbsolutePath() }).run(),
				is(0));
	}

	@Test
	public void testTester() throws Exception {
		assertThat(new CompileRunner(sourceFile).run(), is(0));
		
		TestRunner testRunner = new TestRunner(compiledFile, 1, 2000, 64);
		assertThat(testRunner.run(), is(0));
		CommandRunner outputCommand = new CommandRunner("cat", new String[] {testOutputFile.getAbsolutePath()});
		assertThat(outputCommand.run(), is(0));
		assertThat(outputCommand.getOutput(), is("15\n"));
	}

	@After
	public void after() {
		compiledFile.delete();
		testInputFile.delete();
		testOutputFile.delete();
		testErrorFile.delete();
	}

}
