package org.pesho.judge.controllers;

import java.util.Optional;

import org.pesho.judge.UserService;
import org.pesho.judge.daos.AddAssignmentDao;
import org.pesho.judge.repositories.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AssignmentController {

	@Autowired
	private UserService userService;

	@Autowired
	private AssignmentRepository repository;

	@GetMapping("/assignments")
	public String assignments(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		return "assignments";
	}
	
	@GetMapping("/addassignment")
	public String addassignment(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		return "addassignment";
	}
	
	@PostMapping("/addassignment")
	public String createAssignment(AddAssignmentDao assignment) {
		repository.createAssignment(assignment);

		return "redirect:/assignments.html";
	}
	
}
