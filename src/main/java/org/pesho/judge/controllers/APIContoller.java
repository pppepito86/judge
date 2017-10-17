package org.pesho.judge.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class APIContoller {

	@Autowired
	private JdbcTemplate template;	

	@RequestMapping("/roles")
	public List<Map<String, Object>> roles() {
		return template.queryForList("select * from roles");
	}
	
	@RequestMapping("/users")
	public List<Map<String, Object>> users() {
		return template.queryForList("select * from users");
	}

	@RequestMapping("/userroles")
	public List<Map<String, Object>> userroles() {
		return template.queryForList("select * from userroles");
	}
	
	@RequestMapping("/groups")
	public List<Map<String, Object>> groups() {
		return template.queryForList("select * from groups");
	}
	
	@RequestMapping("/usergroups")
	public List<Map<String, Object>> usergroups() {
		return template.queryForList("select * from usergroups");
	}
	
	@RequestMapping("/problems")
	public List<Map<String, Object>> problems() {
		return template.queryForList("select * from problems");
	}
	
	@RequestMapping("/assignments")
	public List<Map<String, Object>> assignments() {
		return template.queryForList("select * from assignments");
	}

}
