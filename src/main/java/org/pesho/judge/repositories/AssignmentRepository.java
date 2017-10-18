package org.pesho.judge.repositories;

import org.pesho.judge.UserService;
import org.pesho.judge.daos.AddAssignmentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
public class AssignmentRepository {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private UserService userService;

    public List<Map<String,Object>> listUsers() {
        return template.queryForList(
                "select users.id, roleid, username, firstname, lastname, email, passwordhash, passwordsalt, isdisabled, roles.rolename from users inner join roles on users.roleid = roles.id");
    }

    public void createAssignment(AddAssignmentDao assignment) {
        long authorId = userService.getCurrentUserId();
        template.update("INSERT INTO assignments(name, author, groupid, starttime, endtime, testinfo, standings) VALUES(?, ?, ?, ?, ?, ?, ?)",
                new Object[] {
                        assignment.getName(),
                        authorId,
                        assignment.getGroupid(),
                        null,
                        null,
                        assignment.getTestinfo().orElse("show"),
                        assignment.getStandings().orElse("")
                });
    }

}
