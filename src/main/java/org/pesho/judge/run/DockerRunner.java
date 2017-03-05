package org.pesho.judge.run;

public class DockerRunner extends CommandRunner {

	public static final String IMAGE = "pppepito86/judgebox";
	public static final String BASH = "bash";

	public DockerRunner(String cmd) {
		this(cmd, DEFAULT_TIMEOUT);
	}
	
	public DockerRunner(String cmd, long timeout) {
		this(cmd, null, timeout);
	}
	
	public DockerRunner(String cmd, String workDir, long timeout) {
		super(BASH, new String[]{"-c", "docker run " + IMAGE + " " + cmd}, workDir, timeout);
	}
	
}
