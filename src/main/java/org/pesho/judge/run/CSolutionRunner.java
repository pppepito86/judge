package org.pesho.judge.run;

import org.pesho.judge.model.Submission;

public class CSolutionRunner extends SolutionRunner {

	public CSolutionRunner(Submission submission) {
		super(submission);
	}
	
	@Override
	public String getCompileCommand() {
		return "g++ -O2 -std=c++11 -o test " + sourceFile.getName();
	}
	
	@Override
	public String getTestCommand() {
		return "./test";
	}
	
}
