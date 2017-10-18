package org.pesho.judge.controllers;

import java.net.CacheRequest;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.pesho.judge.UserService;
import org.pesho.judge.daos.AddProblemDao;
import org.pesho.judge.repositories.ProblemRepository;
import org.pesho.judge.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProblemController {

	@Autowired
	private UserService userService;

	@Autowired
	private ProblemRepository repository;

	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@GetMapping("/myproblems")
	public String myProblems(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		model.addAttribute("problems", repository.listAuthorProblems(userService.getCurrentUserId()));
		return "problems";
	}

	@PreAuthorize("hasAuthority('admin')")
	@GetMapping("/problems")
	public String problems(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		model.addAttribute("problems", repository.listProblems());
		return "problems";
	}

	@GetMapping("/problem")
	public String problem(@RequestParam("id") long id, Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		model.addAttribute("user", userService.getCurrentUserName());
		model.addAttribute("problem", repository.getProblem(id));
		return "problem";
	}

	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@GetMapping("/addproblem")
	public String addProblem(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		return "addproblem";
	}

	@PreAuthorize("hasAnyAuthority({'admin','teacher'})")
	@PostMapping("/addproblem")
	public String createProblem(AddProblemDao problem) {
		repository.createProblem(problem);
	    return "redirect:/problems.html";
	}
	
}
