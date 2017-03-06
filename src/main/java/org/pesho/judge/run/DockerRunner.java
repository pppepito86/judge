package org.pesho.judge.run;

import java.io.File;
import java.io.IOException;

public class DockerRunner extends CommandRunner {

	public static final String IMAGE = "pppepito86/judgebox";
	public static final String BASH = "/bin/bash";

	private long timeout;

	public DockerRunner(String cmd) {
		this(cmd, 5000, 64);
	}

	public DockerRunner(String cmd, String workDir) {
		this(cmd, workDir, 5000, 64);
	}

	public DockerRunner(String cmd, long timeout, int memory) {
		this(cmd, new File(".").getAbsolutePath(), timeout, memory);
	}

	public DockerRunner(String cmd, String workDir, long timeout, int memory) {
		super("bash", new String[] { "-c", "docker run -v " + workDir + ":/foo -w /foo --read-only --memory=" + memory
				+ "m " + IMAGE + " bash -c \"echo started && time  " + cmd + "\"" }, workDir, timeout + 3000);

		this.timeout = timeout;
	}

	@Override
	public void start() throws IOException {
		super.start();
		new Thread() {
			@Override
			public void run() {
				try {
					for (int i = 0; i < 50; i++) {
						if (getOutput().length() != 0) {
							setTimeout(timeout);
							break;
						}
						Thread.sleep(100);
					}
				} catch (Exception e) {
				}
			}
		}.start();
	}

	@Override
	public int waitFor() throws InterruptedException {
		int exitCode = super.waitFor();
		if (exitCode == 0 && executionTime() > timeout) {
			exitCode = 137;
		}
		return exitCode;
	}

	@Override
	public String getOutput() {
		return super.getOutput().replace("started\n", "");
	}

	public long executionTime() {
		String[] errorOuput = getError().split("\n");
		for (String line : errorOuput) {
			if (line.contains("real")) {
				String[] time = line.replace("real", "").trim().split("m");
				int mins = Integer.parseInt(time[0]);
				int secs = Integer.parseInt(time[1].split("\\.")[0]);
				int millis = Integer.parseInt(time[1].split("\\.")[1].substring(0, 3));
				long totalTime = mins * 60 * 1000 + secs * 1000 + millis;
				return totalTime;
			}
		}
		return -1;
	}

}
