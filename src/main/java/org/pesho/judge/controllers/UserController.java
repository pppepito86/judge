package org.pesho.judge.controllers;

import org.pesho.judge.UserService;
import org.pesho.judge.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository repository;

	@PreAuthorize("hasAuthority('admin')")
	@GetMapping("/users")
	public String users(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		model.addAttribute("users", repository.listUsers());
		return "users";
	}
	
}
