package org.pesho.judge.rest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.UserService;
import org.pesho.judge.dtos.AddAssignmentDto;
import org.pesho.judge.repositories.AssignmentRepository;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AssignmentsRestService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AssignmentRepository repository;
	
	@GetMapping("/assignments")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
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
	public List<Map<String, Object>> listGroupAssignments(
			@PathVariable("group_id") int groupId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		return repository.listGroupAssignments(groupId, page, size);
	}

	@GetMapping("/assignments/users/{user_id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	public List<Map<String, Object>> listUserAssignments(
			@PathVariable("user_id") int userId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		return repository.listUserAssignments(userId, page, size);
	}
	
	@GetMapping("/assignments/authors/{author_id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher','user'})")
	public List<Map<String, Object>> listAuthorAssignments(
			@PathVariable("author_id") int authorId,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		return repository.listAuthorAssignments(authorId, page, size);
	}
	
	@GetMapping("/assignments/{id}")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	public ResponseEntity<?> getAssignment(@PathVariable("id") int id) {
		Optional<Map<String,Object>> assignment = repository.getAssignment(id);
		if (assignment.isPresent()) {
			if (userService.isAdmin() || userService.getCurrentUserId() == assignment.get().get("author")) {
				return new ResponseEntity<>(assignment.get(), HttpStatus.OK);
			} else {
		        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		} else {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/assignments/{id}/problems")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	public List<Map<String, Object>> getAssignmentProblems(@PathVariable("id") int id) {
		return repository.listAssignmentProblems(id);
	}

	@PostMapping("/assignments")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.CREATED)
	public int createAssignment(@RequestBody AddAssignmentDto assignment) {
		return repository.createAssignment(assignment);
	}
	
}
