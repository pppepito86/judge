package org.pesho.judge.run;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeThat;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CompilerTest {
	
	private File sourceFile;
	private File destFile;
	
	@Before
	public void before() throws Exception {
		assumeFalse(System.getProperty("os.name").toLowerCase().contains("win"));
		int exitCode = new CommandRunner("bash", new String[] {"-c", "which docker" }, 5000).run();
		assumeThat(exitCode, is(0));
		
		sourceFile = new File("src/test/resources/docker/submission/Sum.java");
		destFile = new File("src/test/resources/docker/submission/Sum.class");
		assumeFalse(destFile.exists());
	}
	
	@Test
	public void testCompile() throws Exception {
		CompileRunner compiler = new CompileRunner(sourceFile);
		int exitCode = compiler.run();
		assertThat(exitCode, is(0));
		assertTrue(destFile.exists());
		assertTrue(destFile.isFile());
	}
	
	@After
	public void after() {
		destFile.delete();
	}

}
