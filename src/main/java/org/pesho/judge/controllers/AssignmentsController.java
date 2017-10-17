package org.pesho.judge.controllers;

import java.util.Optional;

import org.pesho.judge.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AssignmentsController {

	@Autowired
	private JdbcTemplate template;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/assignments", method=RequestMethod.GET)
	public String assignments(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		return "assignments";
	}
	
	@RequestMapping(value="/addassignment", method=RequestMethod.GET)
	public String addassignment(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		return "addassignment";
	}
	
	@Transactional
	@RequestMapping(value="/addassignment", method=RequestMethod.POST)
	public String createAssignment(
			@RequestParam("assignmentname") String name,
			@RequestParam("problem1") String problems,
			@RequestParam("groupid") String groupId,
			@RequestParam("test-info") Optional<String> testInfo,
			@RequestParam("standings") Optional<String> standings) {
		long authorId = userService.getCurrentUserId();
		template.update("INSERT INTO assignments(name, author, groupid, starttime, endtime, testinfo, standings) VALUES(?, ?, ?, ?, ?, ?, ?)",
				new Object[] {name, authorId, groupId, null, null, testInfo.orElse("show"), standings.orElse("")});
		
		System.out.println(name);
		System.out.println(problems);
		System.out.println(groupId);
		System.out.println(testInfo);

	    return "redirect:/assignments.html";
	}
	
}
