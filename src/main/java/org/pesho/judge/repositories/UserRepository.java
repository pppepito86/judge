package org.pesho.judge.repositories;

import org.pesho.judge.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Configuration
public class UserRepository {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private UserService userService;

    public List<Map<String,Object>> listUsers() {
        return template.queryForList(
                "select users.id, roleid, username, firstname, lastname, email, passwordhash, passwordsalt, isdisabled, roles.rolename from users inner join roles on users.roleid = roles.id");
    }
}
