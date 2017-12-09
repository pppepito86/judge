package org.pesho.judge.rest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.UserService;
import org.pesho.judge.daos.AddAssignmentDao;
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
	public List<Map<String, Object>> listAssignments() {
		if (userService.isAdmin()) {
			return repository.listAssignments();
		} else if (userService.isTeacher()) {
			return repository.listAuthorAssignments(userService.getCurrentUserId());
		} else {
			return repository.listUserAssignments(userService.getCurrentUserId());
		}
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

	@PostMapping("/assignments")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.CREATED)
	public int createAssignment(@RequestBody AddAssignmentDao assignment) {
		return repository.createAssignment(assignment);
	}
	
}
