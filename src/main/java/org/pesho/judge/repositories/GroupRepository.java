package org.pesho.judge.repositories;

import org.pesho.judge.UserService;
import org.pesho.judge.daos.AddGroupDao;
import org.pesho.judge.daos.JoinGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@Configuration
public class GroupRepository {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private UserService userService;

    public List<Map<String,Object>> listGroups() {
        return template.queryForList("select * from groups inner join users on groups.creatorid = users.id");
    }

    public void createGroup(AddGroupDao group) {
        Long userId = userService.getCurrentUserId();
        template.update("INSERT INTO groups(groupname, description, creatorid) VALUES(?, ?, ?)",
                new Object[]{ group.getGroupname(), group.getDescription(), userId});
    }

    public void joinGroup(JoinGroupDao group) {
        Long userId = userService.getCurrentUserId();
        Long groupId = template.queryForObject("select id from groups where groupname=?",
                new Object[] {group.getGroupname()}, Long.class);
        template.update("INSERT INTO usergroups(userid, groupid, roleid) VALUES(?, ?, ?)",
                new Object[]{ userId, groupId, 0});
    }

}
