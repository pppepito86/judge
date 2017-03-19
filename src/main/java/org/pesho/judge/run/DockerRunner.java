package org.pesho.judge.run;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DockerRunner extends CommandRunner {

	public static final String IMAGE = "pppepito86/judgebox";
	public static final String BASH = "/bin/bash";
	public static final String CONTAINER_COMMAND = "bash -c \"echo started && time %s\"";
	public static final String DOCKER_COMMAND = "docker run %s %s";

	private long originalTimeout;

	public DockerRunner(String cmd) {
		this(cmd, 5000, 64);
	}

	public DockerRunner(String cmd, String workDir) {
		this(cmd, workDir, 5000, 64);
	}

	public DockerRunner(String cmd, long timeout, int memory) {
		this(cmd, new File(".").getAbsolutePath(), timeout, memory);
	}

	public DockerRunner(String cmd, String workDir, long timeout, int memory, String... files) {
		this.cmd = "bash";
		String containerCmd = String.format(CONTAINER_COMMAND, cmd);
		List<String> dockerArgsList = new ArrayList<>(10);
		dockerArgsList.add("--cidfile cid");
		for (int i = 0; i < files.length; i++) {
			File file = new File(workDir, files[i]);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			dockerArgsList.add("-v " + workDir + "/" + files[i] + ":/foo/" + files[i]);
		}
		if (files.length == 0) {
			dockerArgsList.add("-v " + workDir + ":/foo");
		}
		dockerArgsList.add("-w /foo");
		dockerArgsList.add("--read-only");
		dockerArgsList.add("--network none");
		dockerArgsList.add("--memory=" + memory + "m");
		dockerArgsList.add(IMAGE);
		String dockerArgs = "";
		for (String dockerArg : dockerArgsList) {
			dockerArgs += " " + dockerArg;
		}
		String dockerCmd = String.format(DOCKER_COMMAND, dockerArgs, containerCmd);

		this.args = new String[] { "-c", dockerCmd };
		System.out.println("docker command is: " + dockerCmd);
		this.workDir = new File(workDir);
		this.timeout = timeout + 3000;
		this.originalTimeout = timeout;
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
							setTimeout(originalTimeout);
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
		if (exitCode == 0 && executionTime() > originalTimeout) {
			exitCode = 137;
		}
		try {
			new CommandRunner("bash", new String[]{"-c", "docker kill -s KILL $(cat cid)"}, workDir.getAbsolutePath(), 5000).run();
			new CommandRunner("rm", new String[]{"cid"}, workDir.getAbsolutePath(), 5000).run();
		} catch (IOException e) {
			e.printStackTrace();
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
