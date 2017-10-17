package org.pesho.judge.controllers;

import org.pesho.judge.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GroupsController {
	
	@Autowired
	private JdbcTemplate template;

	@Autowired
	private UserService userService;
	
	@RequestMapping("/groups")
	public String groups(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		model.addAttribute("groups", template.queryForList(
				"select * from groups inner join users on groups.creatorid = users.id"));
		
		return "groups";
	}
	
	@RequestMapping(value="/addgroup", method=RequestMethod.GET)
	public String addgroup(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		return "addgroup";
	}
	
	@RequestMapping(value="/joingroup", method=RequestMethod.GET)
	public String joingroup(Model model) {
		model.addAttribute("role", userService.getCurrentUserRole());
		return "joingroup";
	}

	@RequestMapping(value="/addgroup", method=RequestMethod.POST)
	public String createGroup(@RequestBody MultiValueMap<String,String> p) {
		Long userId = userService.getCurrentUserId();
		template.update("INSERT INTO groups(groupname, description, creatorid) VALUES(?, ?, ?)", 
				new Object[]{ p.getFirst("groupname"), p.getFirst("description"), userId});
	     return "redirect:/groups.html";
	}
	
	@Transactional
	@RequestMapping(value="joingroup", method=RequestMethod.POST)
	public String joinGroup(@RequestBody MultiValueMap<String,String> p) {
		Long userId = userService.getCurrentUserId();
		Long groupId = template.queryForObject("select id from groups where groupname=?", 
				new Object[] {p.getFirst("groupname")}, Long.class);
		template.update("INSERT INTO usergroups(userid, groupid, roleid) VALUES(?, ?, ?)", 
				new Object[]{ userId, groupId, 0});
	     return "redirect:/groups.html";
	}

}
