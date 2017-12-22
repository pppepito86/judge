package org.pesho.judge.repositories;

import static org.pesho.judge.repositories.SqlUtil.limit;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.pesho.judge.UserService;
import org.pesho.judge.dtos.AddGroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class GroupRepository {

	@Autowired
	private JdbcTemplate template;

	@Autowired
	private UserService userService;

	public List<Map<String, Object>> listGroups(int page, int size) {
		return template.queryForList(
				"select groups.id, groups.groupname, groups.description, groups.creatorid, users.username from groups" + 
				" inner join users on groups.creatorid = users.id" + 
				" order by id desc " + limit(page, size));
	}
	
	public List<Map<String, Object>> listGroupsForTeacher(int teacherId, int page, int size) {
		return template.queryForList(
				"select groups.id, groups.groupname, groups.description, groups.creatorid, users.username from groups" +
				" inner join users on groups.creatorid = users.id where groups.creatorid=?" +
				" order by id desc " + limit(page, size),
				teacherId);
	}

	public List<Map<String, Object>> listGroupsForUser(int userId, int page, int size) {
		return template.queryForList("select groups.id, groupname, description, creatorid, users.username from groups" +
				" inner join usergroups on groups.id=usergroups.groupid and usergroups.userid=?" +
				" inner join users on groups.creatorid = users.id" +
				" order by id desc " + limit(page, size), 
				userId);
	}

	public Optional<Map<String, Object>> getGroup(int groupId) {
		return template.queryForList("select groups.id, groupname, description, creatorid, users.username from groups" +
				" inner join users on groups.creatorid = users.id where groups.id=?",
				groupId).stream().findFirst();
	}
	
	public Optional<Map<String, Object>> getGroup(String groupName) {
		return template.queryForList("select id, groupname, description, creatorid from groups where groupname=?",
				groupName).stream().findFirst();
	}

	@Transactional
	public synchronized int createGroup(AddGroupDto group) {
		template.update("INSERT INTO groups(groupname, description, creatorid) VALUES(?, ?, ?)",
				group.getGroupname(), group.getDescription(), userService.getCurrentUserId());
		
		Optional<Object> first = template.queryForList("SELECT MAX(id) FROM groups").stream()
				.map(x -> x.get("MAX(id)")).findFirst();
		return (int) first.get();
	}

	@Transactional
	public synchronized int updateGroup(int groupId, AddGroupDto group) {
		return template.update("UPDATE groups set groupname=?, description=? where id=?",
				group.getGroupname(), group.getDescription(), groupId);
	}
	
	@Transactional
	public void addGroupUser(String groupName) {
		Object groupId = getGroup(groupName).map(x -> x.get("id")).orElseThrow(() -> new IllegalStateException());
		template.update("INSERT INTO usergroups(userid, groupid, roleid) VALUES(?, ?, ?)",
				userService.getCurrentUserId(), groupId, 0);
	}
	
	public List<Map<String, Object>> studentsForTeacher(int teacherId) {
		return template.queryForList("select distinct u.id, u.roleid, u.username, u.firstname, u.lastname, u.email, u.isdisabled, r.rolename from usergroups as ug"+
				" inner join users as u on u.id = ug.userid"+
				" inner join groups as g on ug.groupid=g.id and g.creatorid = ?"+
				" inner join roles as r on u.roleid = r.id", 
				teacherId);
	}
	
	public List<Map<String, Object>> studentsInGroup(int groupId) {
		return template.queryForList("select distinct u.id, u.roleid, u.username, u.firstname, u.lastname, u.email, u.isdisabled, r.rolename from usergroups as ug"+
				" inner join users as u on u.id = ug.userid and ug.groupid = ? "+
				" inner join roles as r on u.roleid = r.id",
				groupId);
	}

}
