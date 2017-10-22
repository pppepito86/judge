package org.pesho.judge.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AssignmentsRestService {

	@Autowired
	private JdbcTemplate template;	

	@GetMapping("/assignments")
	public List<Map<String, Object>> listAssignments() {
		return template.queryForList(
				"select assignments.id, assignments.name, assignments.author, assignments.groupid, users.username, groups.groupname, assignments.starttime, assignments.endtime, assignments.prejudge_checks from assignments" +
				" inner join users on assignments.author = users.id" +
				" inner join groups on assignments.groupid = groups.id");
	}
	
	@GetMapping("/assignments?author={author_id}")
	public List<Map<String, Object>> listAuthorAssignments(@PathParam("author_id") int authorId) {
		return template.queryForList(
				"select assignments.id, assignments.name, assignments.author, assignments.groupid, users.username, groups.groupname, assignments.starttime, assignments.endtime, assignments.prejudge_checks from assignments"+
				" inner join users on assignments.author = users.id and assignments.author = ?"+
				" inner join groups on assignments.groupid = groups.id", authorId);
	}
	
	@GetMapping("/assignments?user={user_id}")
	public List<Map<String, Object>> listUserAssignments(@PathParam("user_id") int userId) {
		return template.queryForList(
				"select assignments.id, assignments.name, assignments.author, assignments.groupid, users.username, groups.groupname, assignments.starttime, assignments.endtime, assignments.prejudge_checks from assignments"+
				" inner join users on assignments.author = users.id"+
				" inner join groups on assignments.groupid = groups.id"+
				" inner join usergroups on assignments.groupid = usergroups.groupid and usergroups.userid = ?", 
				new Object[] {userId});
	}
	
	@GetMapping("/assignments/{id}")
	public List<Map<String, Object>> getAssignment(@PathParam("id") int id) {
		return template.queryForList(
				"select assignments.id, assignments.name, assignments.author, assignments.groupid, users.username, groups.groupname, assignments.starttime, assignments.endtime, assignments.testinfo, assignments.standings, assignments.prejudge_checks from assignments"+
				" inner join users on assignments.id=? and assignments.author = users.id"+
				" inner join groups on assignments.groupid = groups.id", new Object[] {id});
		
	}

}
