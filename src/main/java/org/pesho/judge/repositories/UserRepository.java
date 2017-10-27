package org.pesho.judge.repositories;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.pesho.judge.daos.AddUserDao;
import org.pesho.judge.daos.EditRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;

@Configuration
public class UserRepository {

    @Autowired
    private JdbcTemplate template;

    public List<Map<String,Object>> listUsers() {
        return template.queryForList(
                "select users.id, roleid, username, firstname, lastname, email, isdisabled, roles.rolename from users inner join roles on users.roleid = roles.id");
    }
    
    public List<Map<String,Object>> listUsers(String username) {
        return template.queryForList(
                "select users.id, roleid, username, firstname, lastname, email, isdisabled, roles.rolename from users inner join roles on users.roleid = roles.id");
    }
    
	public void createUser(@RequestBody AddUserDao user) {
		template.update("INSERT INTO users(roleid, username, firstname, lastname, email, passwordhash, passwordsalt, isdisabled, validationcode) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)", 
				new Object[] {
						3, 
						user.getUsername(),
						user.getFirstname(),
						user.getLastname(),
						user.getEmail(),
						user.getPassword(),
						user.getPassword(),
						false,
						UUID.randomUUID().toString()});
	}
	
	public void updateRole(long userId, EditRoleDao role) {
		template.update("update users set roleid=? where id=?", new Object[] {role.getRoleid(), userId});
	}
	
	public List<Map<String, Object>> studentsForTeacher(long teacherId) {
		return template.queryForList("select distinct u.id, u.roleid, u.username, u.firstname, u.lastname, u.email, u.isdisabled, r.rolename from usergroups as ug"+
				" inner join users as u on u.id = ug.userid"+
				" inner join groups as g on ug.groupid=g.id and g.creatorid = ?"+
				" inner join roles as r on u.roleid = r.id", 
				new Object[] {teacherId});
	}
	
	public void validateUser(long code) {
		template.update("update users set validationcode='' where validationcode=?", new Object[] {code});
	}

	
}