package org.pesho.judge.run;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.pesho.judge.checker.CompileRunner;

public class CompilerTest {
	
	@Before
	public void beforeMethod() throws Exception {
		assumeFalse(System.getProperty("os.name").toLowerCase().contains("win"));
		int exitCode = new CommandRunner("bash", new String[] {"-c", "which docker" }, 5000).run();
		assumeThat(exitCode, is(0));
	}
	
	@Test
	public void testCompile() throws Exception {
		File sourceFile = new File("src/test/resources/docker/Sum.java");
		File destFile = new File("src/test/resources/docker/Sum.class");

		CompileRunner compiler = new CompileRunner(sourceFile);
		int exitCode = compiler.run();
		assertThat(exitCode, is(0));
		assertTrue(destFile.isFile());
	}

}
