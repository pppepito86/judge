package org.pesho.judge.rest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.PathParam;

import org.pesho.judge.UserService;
import org.pesho.judge.daos.AddProblemDao;
import org.pesho.judge.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProblemsRestService {

	@Autowired
	private ProblemRepository repository;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/problems")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	public List<Map<String, Object>> listProblems() {
		if (userService.isAdmin()) {
			return repository.listProblems();
		} else {
			return repository.listProblems(userService.getCurrentUserId());			
		}
	}
	
	@GetMapping("/myproblems")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	public List<Map<String, Object>> listMyProblems() {
		return repository.listAuthorProblems(userService.getCurrentUserId());
	}
	
	@GetMapping("/problems/{problem_id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	public ResponseEntity<?> getProblem(@PathVariable("problem_id") long problem_id) {
		Optional<Map<String,Object>> problem = repository.getProblem(problem_id);
		if (problem.isPresent()) {
			if (userService.isAdmin() 
					|| "public".equals(problem.get().get("visibility"))
					|| userService.getCurrentUserId().equals(problem.get().get("id"))) {
				return new ResponseEntity<>(problem.get(), HttpStatus.OK);
			} else {
		        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		} else {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
	
	@PostMapping("/problems")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@ResponseStatus(HttpStatus.CREATED)
	public void createProblem(@RequestBody AddProblemDao problem) {
		repository.createProblem(problem);
	}
	
	@PutMapping("/problems/{problem_id}/tags")
	public void createTag(@PathParam("problem_id") long problemId, String tag) {
		repository.createTag(problemId, tag);
	}
	
	@DeleteMapping("/problems/{problem_id}/tags")
	public void deleteTags(@PathParam("problem_id") long problemId) {
		repository.deleteTags(problemId);
	}

}
