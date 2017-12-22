package org.pesho.judge.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.dtos.AddUserDto;
import org.pesho.judge.dtos.CheckUserDto;
import org.pesho.judge.repositories.UserRepository;
import org.pesho.judge.security.JudgeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicRestService {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JudgeUserDetailsService judgeUserDetailsService;
	
	@PostMapping("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.CREATED)
	public void createUser(@RequestBody AddUserDto user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.createUser(user);
	}

	@PostMapping("/login")
	public ResponseEntity<?> checkUser(@RequestBody CheckUserDto user) {
		UserDetails userDetails = judgeUserDetailsService.loadUserByUsername(user.getUsername());
		if (userDetails != null && passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
}
