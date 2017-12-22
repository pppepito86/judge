package org.pesho.judge.rest;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.UserService;
import org.pesho.judge.dtos.AddAssignmentDto;
import org.pesho.judge.repositories.AssignmentRepository;
import org.pesho.judge.repositories.ProblemRepository;
import org.pesho.judge.repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1")
public class AssignmentsRestService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AssignmentRepository repository;
	
	@Autowired
	private ProblemRepository problemsRepository;

	@Autowired
	private SubmissionRepository submissionsRepository;
	
	@GetMapping("/assignments")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> listAssignments(
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		if (userService.isAdmin()) {
			return repository.listAllAssignments(page, size);
		} else if (userService.isTeacher()) {
			return repository.listAuthorAssignments(userService.getCurrentUserId(), page, size);
		} else {
			return repository.listUserAssignments(userService.getCurrentUserId(), page, size);
		}
	}
	
	@GetMapping("/assignments/groups/{group_id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> listGroupAssignments(
			@PathVariable("group_id") int groupId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		return repository.listGroupAssignments(groupId, page, size);
	}

	@GetMapping("/assignments/users/{user_id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> listUserAssignments(
			@PathVariable("user_id") int userId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		return repository.listUserAssignments(userId, page, size);
	}
	
	@GetMapping("/assignments/authors/{author_id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> listAuthorAssignments(
			@PathVariable("author_id") int authorId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		return repository.listAuthorAssignments(authorId, page, size);
	}
	
	@GetMapping("/assignments/{id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<?> getAssignment(@PathVariable("id") int id) {
		Optional<Map<String,Object>> assignment = repository.getAssignment(id);
		if (assignment.isPresent()) {
			return new ResponseEntity<>(assignment.get(), HttpStatus.OK);
		} else {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/assignments/{id}/problems")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, Object>> getAssignmentProblems(@PathVariable("id") int id) {
		return repository.listAssignmentProblems(id);
	}
	
	@GetMapping("/assignments/{id}/points")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAssignmentPoints(@PathVariable("id") int id) {
		List<Map<String, Object>> problems = repository.listAssignmentProblems(id);
		int points = problems.stream()
			.map(p -> submissionsRepository.getBestUserSubmission(userService.getCurrentUserId(), (int) p.get("assignmentid"), (int) p.get("problemid")))
			.filter(s -> s.isPresent())
			.mapToInt(s -> (int) s.get().get("points"))
			.sum();
		int max = problems.stream().mapToInt(p -> (int) p.get("points")).sum();
		return String.format("{\"points\": %d, \"max\" : %d}", points, max);
	}

	@GetMapping("/assignments/{assignment_id}/problems/{problem_id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<?> getAssignmentProblem(
			@PathVariable("assignment_id") int assignmentId,
			@PathVariable("problem_id") int problemNumber) {
		Optional<Map<String,Object>> problem = problemsRepository.getAssignmentProblem(assignmentId, problemNumber);
		if (problem.isPresent()) {
			return new ResponseEntity<>(problem.get(), HttpStatus.OK);
		} else {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
	
	@PostMapping("/assignments")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseEntity<?> createAssignment(@RequestBody AddAssignmentDto assignment) {
		int assignmentId =  repository.createAssignment(assignment);
		
	    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
	    		.path("/{id}").buildAndExpand(assignmentId).toUri();
		return ResponseEntity.created(location).build();
	}
	
}
