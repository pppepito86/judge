package org.pesho.judge.run;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.pesho.judge.model.Submission;

public class SolutionFactoryTest {
	
	private Submission submission = new Submission();
	
	@Test
	public void testJava() {
		submission.setSourceFile("/source/Sum.java");
		SolutionRunner solutionRunner = SolutionFactory.getSolutionRunner(submission);
		assertThat(solutionRunner, instanceOf(JavaSolutionRunner.class));
	}
	
	@Test
	public void testC() {
		submission.setSourceFile("/source/sum.c");
		SolutionRunner solutionRunner = SolutionFactory.getSolutionRunner(submission);
		assertThat(solutionRunner, instanceOf(CSolutionRunner.class));
	}
	
	@Test
	public void testCpp() {
		submission.setSourceFile("/source/sum.cpp");
		SolutionRunner solutionRunner = SolutionFactory.getSolutionRunner(submission);
		assertThat(solutionRunner, instanceOf(CSolutionRunner.class));
	}
	
	@Test
	public void testOther() {
		submission.setSourceFile("/source/sum.other");
		SolutionRunner solutionRunner = SolutionFactory.getSolutionRunner(submission);
		assertThat(solutionRunner, instanceOf(DefaultSolutionRunner.class));
	}

}
