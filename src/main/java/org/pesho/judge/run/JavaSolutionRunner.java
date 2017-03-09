package org.pesho.judge.run;

import org.pesho.judge.model.Submission;

public class JavaSolutionRunner extends SolutionRunner {

	public JavaSolutionRunner(Submission submission) {
		super(submission);
	}
	
	@Override
	public String getCompileCommand() {
		return "javac " + sourceFile.getName();
	}
	
	@Override
	public String getTestCommand() {
		return "java " + sourceFile.getName().replace(".java", "");
	}
	
}
