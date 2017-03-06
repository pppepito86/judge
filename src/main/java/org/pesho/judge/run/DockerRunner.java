package org.pesho.judge.run;

import java.io.File;

public class DockerRunner extends CommandRunner {

	public static final String IMAGE = "pppepito86/judgebox";
	public static final String BASH = "/bin/bash";
	
	public DockerRunner(String cmd) {
		this(cmd, DEFAULT_TIMEOUT);
	}
	
	public DockerRunner(String cmd, long timeout) {
		this(cmd, new File(".").getAbsolutePath(), timeout);
	}
	
	public DockerRunner(String cmd, String workDir, long timeout) {
		super(BASH, new String[]{"-c", "docker run -v "+ workDir + ":/foo -w /foo --read-only " + IMAGE + " " + cmd}, workDir, timeout);
	}
	
}
