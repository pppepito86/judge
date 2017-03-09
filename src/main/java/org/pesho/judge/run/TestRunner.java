package org.pesho.judge.run;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
	
	private DockerRunner runner;
	
	public TestRunner(File compiledFile, int testNumber, int timeout, int memory) {
		this("java " + compiledFile.getName().replace(".class", ""), compiledFile.getParentFile(), testNumber, timeout, memory);
	}
	
	public TestRunner(String testCommand, File workDir, int testNumber, int timeout, int memory) {
		String input = "input" + testNumber;
		String output = "output" + testNumber;
		String error = "error" + testNumber;
		String command = String.format("cat %s|%s >%s 2>%s", input, testCommand, output, error);

		List<String> accessibleFiles = new ArrayList<String>();
		accessibleFiles.add(input);
		accessibleFiles.add(output);
		accessibleFiles.add(error);
		for (File file: workDir.listFiles()) {
			if (file.getName().endsWith(".class")) {
				accessibleFiles.add(file.getName());
			}
			if (file.getName().equals("test")) {
				accessibleFiles.add(file.getName());
			}
		}
		runner = new DockerRunner(command, workDir.getAbsolutePath(), timeout, memory, accessibleFiles.toArray(new String[0]));
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
	
	public long executionTime() {
		return runner.executionTime();
	}
	
	public boolean isTimedOut() {
		return runner.isTimedOut();
	}

}
