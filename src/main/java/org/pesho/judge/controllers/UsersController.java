package org.pesho.judge.controllers;

import org.pesho.judge.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UsersController {
	
	@Autowired
	private JdbcTemplate template;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/users", method=RequestMethod.GET)
	public String users(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		model.addAttribute("users", template.queryForList("select users.id, roleid, username, firstname, lastname, email, passwordhash, passwordsalt, isdisabled, roles.rolename from users inner join roles on users.roleid = roles.id"));
		return "users";
	}
	
}
