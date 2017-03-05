package org.pesho.judge.run;

public class DockerRunner {

	public static final String IMAGE = "pppepito86/judgebox";
	public static final String BASH = "bash";

	private CommandRunner commandRunner;

	public DockerRunner(String cmd, long timeout) {
		String[] args = new String[]{"-c", "docker run " + IMAGE + " " + cmd};
		commandRunner = new CommandRunner(BASH, args, timeout);
	}
	
	public int run() throws Exception {
		return commandRunner.run();
	}
	
	public String getOutput() {
		return commandRunner.getOutput();
	}
	
	public String getError() {
		return commandRunner.getError();
	}

}
