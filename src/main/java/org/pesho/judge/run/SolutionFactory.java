package org.pesho.judge.run;

import java.io.File;

import org.pesho.judge.model.Submission;

public class SolutionFactory {
	
	public static SolutionRunner getSolutionRunner(Submission submission) {
		File sourceFile = new File(submission.getSourceFile());
		String extension = sourceFile.getName().split("\\.")[1];
		switch (extension) {
		case "java":
			return new JavaSolutionRunner(submission);
		case "c":
		case "cpp":
			return new CSolutionRunner(submission);
		default:
			return new DefaultSolutionRunner(submission);
		}
	}

}
