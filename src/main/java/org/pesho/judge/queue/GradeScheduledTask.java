package org.pesho.judge.queue;

import java.io.File;
import java.util.Map;
import java.util.Optional;

import org.pesho.grader.GradeListener;
import org.pesho.grader.SubmissionGrader;
import org.pesho.grader.step.StepResult;
import org.pesho.grader.task.TaskDetails;
import org.pesho.grader.task.TaskParser;
import org.pesho.grader.task.TestCase;
import org.pesho.grader.task.TestGroup;
import org.pesho.judge.repositories.ProblemRepository;
import org.pesho.judge.repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GradeScheduledTask implements GradeListener {

    @Value("${work.dir}")
    private String workDir;
	
	@Autowired
	private SubmissionsQueue queue;
	
	@Autowired
	private ProblemRepository problemRepository;
	
	@Autowired
	private SubmissionRepository submissionRepository;
	
	private Integer submissionId;
	
    @Scheduled(fixedDelay = 100)
	public void gradeTask() {
    	submissionId = queue.poll();
    	if (submissionId == null) return;
    	
    	Optional<Map<String, Object>> submission = submissionRepository.getSubmission(submissionId);
    	if (!submission.isPresent()) return;
    	
    	int problemId = (int) submission.get().get("problemid");
    	Optional<Map<String, Object>> problem = problemRepository.getProblem(problemId);
    	if (!problem.isPresent()) return;

    	String sourceFile = (String) submission.get().get("sourcefile");
    	
    	int points = (int) problem.get().get("points");
    	
    	File submissionsDir = new File(workDir, "submissions");
    	File submissionDir = new File(submissionsDir, String.valueOf(submissionId));
    	File sourceFileFile = new File(submissionDir, sourceFile);
    	
    	
    	File problemsDir = new File(workDir, "problems");
    	File problemDir = new File(problemsDir, String.valueOf(problemId));
    	
    	TaskParser taskParser = new TaskParser(problemDir);
		TestCase[] testCases = new TestCase[taskParser.testsCount()];
		for (int i = 0; i < testCases.length; i++) {
			testCases[i] = new TestCase(i+1, taskParser.getInput().get(i).getAbsolutePath(), taskParser.getOutput().get(i).getAbsolutePath());
		}
		TestGroup[] testGroups = new TestGroup[testCases.length];
		for (int i = 0; i < testGroups.length; i++) {
			testGroups[i] = new TestGroup(1.0/testCases.length, testCases[i]);
		}
		TaskDetails taskDetails = new TaskDetails(points, taskParser.getChecker().getAbsolutePath(), testGroups);

    	SubmissionGrader grader = new SubmissionGrader(taskDetails, sourceFileFile.getAbsolutePath(), this);
    	grader.grade();
	}
    
    @Override
    public void addScoreStep(String step, StepResult result) {
    	String reason = result.getReason()!=null?result.getReason():"";
    	submissionRepository.addSubmissionDetails(submissionId, step, result.getVerdict().toString(), reason, 0);
    }
    
    @Override
    public void addScore(double score) {
    	submissionRepository.updateVerdict(submissionId, "", "", 0, 0, (int) Math.round(score));
    }
	
}
