package org.pesho.judge.run;

import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeThat;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pesho.judge.model.Problem;
import org.pesho.judge.model.Submission;

public class JavaSolutionTest {

	private Submission submission;

	@Before
	public void before() throws Exception {
		assumeFalse(System.getProperty("os.name").toLowerCase().contains("win"));
		int exitCode = new CommandRunner("bash", new String[] { "-c", "which docker" }, 5000).run();
		assumeThat(exitCode, is(0));
		
		String sourceFile = "src/test/resources/workdir/submissions/1/Sum.java";
		Problem problem = new Problem();
		problem.setId(1);
		problem.setTests(5);
		submission = new Submission();
		submission.setId(1);
		submission.setPoints(100);
		submission.setSourceFile(sourceFile);
		submission.setProblem(problem);
	}

	@Test
	public void testTester() throws Exception {
		SolutionRunner solver = SolutionFactory.getSolutionRunner(submission);
		solver.run();
	}

	@After
	public void after() throws Exception {
		String testDir = new File("src/test/resources/workdir/submissions/1/test").getAbsolutePath();
		new CommandRunner("bash", new String[]{"-c", "rm -rf " + testDir}).run();
	}

}
