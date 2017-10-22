package org.pesho.judge.repositories;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.pesho.judge.UserService;
import org.pesho.judge.daos.AddGroupDao;
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

	public List<Map<String, Object>> listGroups() {
		return template.queryForList(
				"select groups.id, groups.groupname, groups.description, groups.creatorid, users.username from groups"
						+ " inner join users on groups.creatorid = users.id");
	}
	
	public List<Map<String, Object>> listGroupsForTeacher(long teacherId) {
		return template.queryForList(
				"select groups.id, groups.groupname, groups.description, groups.creatorid, users.username from groups"
						+ " inner join users on groups.creatorid = users.id where groups.creatorid=?",
						new Object[] {teacherId});
	}

	public List<Map<String, Object>> listGroupsForUser(long userId) {
		return template.queryForList("select groups.id, groupname, description, creatorid, users.username from groups"
				+ " inner join usergroups on groups.id=usergroups.groupid and usergroups.userid=?"
				+ " inner join users on groups.creatorid = users.id", new Object[] { userId });
	}

	public Optional<Map<String, Object>> getGroup(String groupName) {
		return template.queryForList("select id, groupname, description, creatorid from groups where groupname=?",
				new Object[] { groupName }).stream().findFirst();
	}

	public void createGroup(AddGroupDao group) {
		template.update("INSERT INTO groups(groupname, description, creatorid) VALUES(?, ?, ?)",
				new Object[] { group.getGroupname(), group.getDescription(), userService.getCurrentUserId() });
	}

	@Transactional
	public void addGroupUser(String groupName) {
		Object groupId = getGroup(groupName).map(x -> x.get("id")).orElseThrow(() -> new IllegalStateException());
		template.update("INSERT INTO usergroups(userid, groupid, roleid) VALUES(?, ?, ?)",
				new Object[] {userService.getCurrentUserId(), groupId, 0});
	}
	
	public List<Map<String, Object>> studentsForTeacher(long teacherId) {
		return template.queryForList("select distinct u.id, u.roleid, u.username, u.firstname, u.lastname, u.email, u.isdisabled, r.rolename from usergroups as ug"+
				" inner join users as u on u.id = ug.userid"+
				" inner join groups as g on ug.groupid=g.id and g.creatorid = ?"+
				" inner join roles as r on u.roleid = r.id", 
				new Object[] {teacherId});
	}
	
	public List<Map<String, Object>> studentsInGroup(long groupId) {
		return template.queryForList("select distinct u.id, u.roleid, u.username, u.firstname, u.lastname, u.email, u.isdisabled, r.rolename from usergroups as ug"+
				" inner join users as u on u.id = ug.userid and ug.groupid = ? "+
				" inner join roles as r on u.roleid = r.id",
				new Object[] {groupId});
	}

}
