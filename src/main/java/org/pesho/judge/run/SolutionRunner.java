package org.pesho.judge.run;

import java.io.File;

import org.pesho.judge.checker.DefaultChecker;
import org.pesho.judge.checker.EmptySubmissionStatus;
import org.pesho.judge.checker.SubmissionStatus;
import org.pesho.judge.model.Submission;

public abstract class SolutionRunner {

	protected Submission submission;
	protected SubmissionStatus status;
	protected File originalSourceFile;
	protected File sourceFile;
	protected File testDir;

	public SolutionRunner(Submission submission) {
		this(submission, new EmptySubmissionStatus());
	}

	public SolutionRunner(Submission submission, SubmissionStatus status) {
		this.submission = submission;
		this.status = status;
		this.originalSourceFile = new File(submission.getSourceFile());
		this.testDir = new File(originalSourceFile.getParentFile(), "test");
		this.sourceFile = new File(testDir, originalSourceFile.getName());
	}

	public void run() {
		try {
			createTestDir();
			compile();
			test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void createTestDir() {
		testDir.mkdirs();
	}

	private void compile() throws Exception {
		new CommandRunner("cp", new String[] { originalSourceFile.getAbsolutePath(), sourceFile.getAbsolutePath() })
				.run();
		System.out.println("Compiling submission <" + submission.getId() + ">");
		DockerRunner compileRunner = new DockerRunner(getCompileCommand(), testDir.getAbsolutePath());
		int exitCode = compileRunner.run();
		if (exitCode != 0) {
			status.updateVerdict("Compilation Failed", "error", "", compileRunner.executionTime());
		}
		System.out.println("Compilation finished");
	}

	public abstract String getCompileCommand();

	public abstract String getTestCommand();

	private void test() throws Exception {
		int tests = submission.getProblem().getTests();
		for (int i = 1; i <= tests; i++) {
			copyInputFile(i);
			test(i);
			copyOutputFile(i);
			checkOutput(i);
		}
	}

	private void test(int testNumber) throws Exception {
		System.out.println("Running test <" + testNumber + ">, for submission <" + submission.getId() + ">");
		TestRunner tester = new TestRunner(getTestCommand(), testDir, testNumber, 1000, 64);
		tester.run();
		System.out.println("Test <" + testNumber + "> finished");
	}

	private void copyInputFile(int testCase) throws Exception {
		copyFile("input" + testCase, "input" + testCase);
	}

	private void copyOutputFile(int testCase) throws Exception {
		copyFile("output" + testCase, "real_output" + testCase);
	}

	private void copyFile(String source, String destination) throws Exception {
		String originalDir = "src/test/resources/workdir/problems/" + submission.getProblem().getId();
		String originalFile = new File(originalDir, source).getAbsolutePath();
		String outputFile = new File(testDir, destination).getAbsolutePath();
		CommandRunner commandRunner = new CommandRunner("bash",
				new String[] { "-c", "cp " + originalFile + " " + outputFile });
		commandRunner.run();
	}

	private void checkOutput(int testCase) throws Exception {
		String[] args = { new File(testDir, "input" + testCase).getAbsolutePath(),
				new File(testDir, "real_output" + testCase).getAbsolutePath(),
				new File(testDir, "output" + testCase).getAbsolutePath() };
		int result = DefaultChecker.check(args);
		System.out.println("Check <" + testCase + "> status is: " + result);

	}

}
