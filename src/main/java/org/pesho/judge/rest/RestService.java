package org.pesho.judge.rest;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.pesho.grader.SubmissionGrader;
import org.pesho.grader.task.TaskTests;
import org.pesho.judge.daos.ProblemDao;
import org.pesho.judge.daos.SubmissionDao;
import org.pesho.judge.problems.ProblemsCache;
import org.pesho.judge.problems.SubmissionsStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class RestService {

	@Value("${work.dir}")
	private String workDir;

	@Autowired
	private ProblemsCache problemsCache;

	@Autowired
	private SubmissionsStorage submissionsStorage;

	@GetMapping("/health-check")
	public String healthCheck() {
		return "ok";
	}

	@GetMapping("/problems")
	public Collection<ProblemDao> listProblems() {
		return problemsCache.listProblems();
	}

	@GetMapping("/problems/{problem_id}")
	public ResponseEntity<?> getProblem(@PathVariable("problem_id") int problemId) {
		ProblemDao problem = problemsCache.getProblem(Integer.valueOf(problemId));
		if (problem != null) {
			return new ResponseEntity<>(problem, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@PostMapping("/problems/{problem_id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ResponseEntity<?> addProblem(@PathVariable("problem_id") String problemId,
			@RequestPart(name = "metadata", required = false) Optional<ProblemDao> problem,
			@RequestPart("file") MultipartFile file) throws Exception {
		try {
			if (problem.isPresent()) {
				problemsCache.addProblem(Integer.valueOf(problemId), problem.get(), file.getInputStream());
			} else {
				problemsCache.addProblem(Integer.valueOf(problemId), file.getInputStream());
			}
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/submissions/{submission_id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ResponseEntity<?> addSubmission(@PathVariable("submission_id") String submissionId,
			@RequestPart(name = "metadata", required = false) Optional<SubmissionDao> submission, @RequestPart("file") MultipartFile file)
			throws Exception {
		try {
			File submissionFile = submissionsStorage.storeSubmission(submissionId, "solve.cpp",
					file.getInputStream());
			TaskTests taskTests = problemsCache.getProbleNew(1);
			SubmissionGrader grader = new SubmissionGrader(taskTests, submissionFile.getAbsolutePath());
			grader.grade();
			return new ResponseEntity<>(grader.getScore(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

}
