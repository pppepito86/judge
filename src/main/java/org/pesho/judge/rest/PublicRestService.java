package org.pesho.judge.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.daos.AddUserDao;
import org.pesho.judge.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicRestService {
	
	@Autowired
	private UserRepository repository;
	
	@PostMapping("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.CREATED)
	public void createUser(@RequestBody AddUserDao user) {
		repository.createUser(user);
	}
	
}
