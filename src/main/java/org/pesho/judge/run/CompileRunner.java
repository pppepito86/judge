package org.pesho.judge.run;

import java.io.File;

public class CompileRunner {
	
	private DockerRunner runner;
	
	public CompileRunner(File sourceFile) {
		String command = "javac " + sourceFile.getName();
		String workDir = sourceFile.getParentFile().getAbsolutePath();
		runner = new DockerRunner(command, workDir);
	}

	public int run() throws Exception {
		int exitCode = runner.run();
		return exitCode;
	}

}
