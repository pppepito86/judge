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
		System.out.println("compile error: " + runner.getError());
		if (exitCode != 0) {
			System.out.println("ERROR");
			Thread.sleep(100000);
		}
		return exitCode;
	}

}
