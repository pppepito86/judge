package org.pesho.judge.run;

import java.io.File;

import org.pesho.judge.checker.DefaultChecker;
import org.pesho.judge.checker.ExactChecker;
import org.pesho.judge.model.Submission;

public class SolutionRunner {

	private Submission submission;
	private File originalSourceFile;
	private File sourceFile;
	private File testDir;

	public SolutionRunner(Submission submission) {
		this.submission = submission;
		this.originalSourceFile = new File(submission.getSourceFile());
		this.testDir = new File(originalSourceFile.getParentFile(), "test");
		this.sourceFile = new File(testDir, originalSourceFile.getName());
	}

	public void run() throws Exception {
		createTestDir();
		compile();
		copyInputFiles();
		test();
		copyOutputFiles();
		checkOutput();
	}

	private void checkOutput() throws Exception {
		File outputDir = new File(testDir.getParentFile(), "output");
		for (int i = 1; i <= submission.getProblem().getTests(); i++) {
			String[] args = { new File(testDir, "input" + i).getAbsolutePath(),
					new File(outputDir, "output" + i).getAbsolutePath(),
					new File(testDir, "output" + i).getAbsolutePath() };
			int result = DefaultChecker.check(args);
			System.out.println("Check <" + i + "> status is: " + result);
		}

	}

	private void createTestDir() {
		testDir.mkdirs();
	}

	private void compile() throws Exception {
		new CommandRunner("cp", new String[] { originalSourceFile.getAbsolutePath(), sourceFile.getAbsolutePath() })
				.run();
		System.out.println("Compiling submission <" + submission.getId() + ">");
		CompileRunner compiler = new CompileRunner(sourceFile);
		compiler.run();
		System.out.println("Compilation finished");
	}

	private void copyInputFiles() throws Exception {
		copyFiles("input");
	}

	private void copyOutputFiles() throws Exception {
		String originalDir = "src/test/resources/workdir/problems/" + submission.getProblem().getId();
		File outputDir = new File(testDir.getParentFile(), "output");
		outputDir.mkdirs();
		for (int i = 1; i <= submission.getProblem().getTests(); i++) {
			String originalFile = new File(originalDir, "output" + i).getAbsolutePath();
			String outputFile = new File(outputDir, "output" + i).getAbsolutePath();
			CommandRunner commandRunner = new CommandRunner("bash",
					new String[] { "-c", "cp " + originalFile + " " + outputFile });
			commandRunner.run();
		}
	}

	private void copyFiles(String prefix) throws Exception {
		String filesPath = "src/test/resources/workdir/problems/" + submission.getProblem().getId() + "/" + prefix
				+ "*";
		String filesDir = new File(filesPath).getAbsolutePath();
		CommandRunner commandRunner = new CommandRunner("bash",
				new String[] { "-c", "cp " + filesDir + " " + testDir.getAbsolutePath() });
		commandRunner.run();
	}

	private void test() throws Exception {
		int tests = submission.getProblem().getTests();
		for (int i = 1; i <= tests; i++) {
			test(i);
		}
	}

	private void test(int testNumber) throws Exception {
		System.out.println("Running test <" + testNumber + ">, for submission <" + submission.getId() + ">");
		File compiledFile = new File(sourceFile.getAbsolutePath().replace(".java", ".class"));
		TestRunner tester = new TestRunner(compiledFile, testNumber, 1000, 64);
		tester.run();
		System.out.println("Test <" + testNumber + "> finished");
	}

}
