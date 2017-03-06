package org.pesho.judge.checker;

import java.io.File;

import org.pesho.judge.run.DockerRunner;

public class TestRunner {
	
	private DockerRunner runner;
	
	public TestRunner(File compiledFile, int testNumber) {
		String javaCommand = "java " + compiledFile.getName().replace(".class", "");
		String input = "input" + testNumber;
		String output = "output" + testNumber;
		String error = "error" + testNumber;
		String command = String.format("cat %s|%s >%s 2>%s", input, javaCommand, output, error);
		System.out.println("Command: " + command);

		String workDir = compiledFile.getParentFile().getAbsolutePath();
		runner = new DockerRunner(command, workDir, 5000);
	}

	public int run() throws Exception {
		int exitCode = runner.run();
		return exitCode;
	}
	
	public String getOutput() {
		return runner.getOutput();
	}
	
	public String getError() {
		return runner.getError();
	}

}
