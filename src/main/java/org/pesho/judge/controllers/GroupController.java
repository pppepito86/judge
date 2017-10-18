package org.pesho.judge.controllers;

import org.pesho.judge.UserService;
import org.pesho.judge.daos.AddGroupDao;
import org.pesho.judge.daos.JoinGroupDao;
import org.pesho.judge.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class GroupController {

	@Autowired
	private UserService userService;

	@Autowired
	private GroupRepository repository;
	
	@GetMapping("/groups")
	public String listGroups(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		model.addAttribute("groups", repository.listGroups());
		
		return "groups";
	}
	
	@GetMapping("/addgroup")
	public String addGroup(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		return "addgroup";
	}
	
	@GetMapping("/joingroup")
	public String joinGroup(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		return "joingroup";
	}

	@PostMapping("/addgroup")
	public String createGroup(@Valid AddGroupDao group) {
		repository.createGroup(group);
		return "redirect:/groups.html";
	}
	
	@PostMapping("joingroup")
	public String joinGroup(JoinGroupDao group) {
		repository.joinGroup(group);
	     return "redirect:/groups.html";
	}

}
