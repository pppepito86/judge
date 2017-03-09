package org.pesho.judge.run;

import org.pesho.judge.model.Submission;

public class DefaultSolutionRunner extends SolutionRunner {

	public DefaultSolutionRunner(Submission submission) {
		super(submission);
	}
	
	@Override
	public String getCompileCommand() {
		return "default";
	}
	
	@Override
	public String getTestCommand() {
		return "default";
	}
	
}
