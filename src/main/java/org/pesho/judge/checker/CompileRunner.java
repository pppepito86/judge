package org.pesho.judge.checker;

import java.io.File;

import org.pesho.judge.run.DockerRunner;

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
