package org.pesho.judge.rest;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.daos.ProblemDao;
import org.pesho.judge.problems.ProblemsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1")
public class RestService {

	@Value("${work.dir}")
	private String workDir;

	@Autowired
	private ProblemsCache problemsCache;

	private ObjectMapper objectMapper = new ObjectMapper();

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
			@RequestParam("file") MultipartFile file, @RequestParam("metadata") MultipartFile metadata)
			throws Exception {
		try {
			ProblemDao problem = objectMapper.readValue(metadata.getInputStream(), ProblemDao.class);
			problemsCache.addProblem(Integer.valueOf(problemId), problem, file.getInputStream());
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (IllegalStateException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

}
