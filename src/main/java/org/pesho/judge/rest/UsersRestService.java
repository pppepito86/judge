package org.pesho.judge.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.UserService;
import org.pesho.judge.daos.AddUserDao;
import org.pesho.judge.daos.EditRoleDao;
import org.pesho.judge.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class UsersRestService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private UserService userService;

	@GetMapping("/users")
	@PreAuthorize("hasAuthority('admin')")
	public List<Map<String, Object>> listUsers() {
		return repository.listUsers();
	}
	
	@GetMapping("/myusers")
	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	public List<Map<String, Object>> listMyUsers() {
		return repository.studentsForTeacher(userService.getCurrentUserId());
	}
		
	@PutMapping("/users/validate={code}")
	public void validateUser(@PathParam("code") long code) {
		repository.validateUser(code);
	}
	
	@PostMapping("/users")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.CREATED)
	public void createUser(@RequestBody AddUserDao user) {
		repository.createUser(user);
	}
	
	@PutMapping("/users/{user_id}/roles")
	@Consumes(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasAuthority('admin')")
	public void updateRole(@PathVariable("user_id") long userId, @RequestBody EditRoleDao role) {
		repository.updateRole(userId, role);
	}
	
}
