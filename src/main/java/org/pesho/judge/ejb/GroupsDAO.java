package org.pesho.judge.ejb;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.pesho.judge.model.Group;
import org.pesho.judge.model.User;
import org.pesho.judge.model.UserGroup;

@Stateless
public class GroupsDAO {

	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
    public List<Group> listGroups() {
        TypedQuery<Group> query = em.createNamedQuery("Group.findAll", Group.class);
    	List<Group> results = query.getResultList();
        return results;
    }
    
    public Group createGroup(Group group) {
    	// TODO;
        return null;
    }
    
    public Group getGroup(int groupId) {
    	Group group = em.find(Group.class, (Integer)groupId);
        return group;
    }
    
    public Group updateGroup(int groupId, Group group) {
    	// TODO
        return null;
    }
    
    public List<User> getUsersFromGroup(int groupId) {
    	Group group = getGroup(groupId);
    	TypedQuery<UserGroup> query = em
    			.createNamedQuery("UserGroup.findByGroup",UserGroup.class)
    			.setParameter("group", group);
    	
    	List<UserGroup> userGroups = query.getResultList();
    	
    	List<User> users = userGroups.stream()
    		.map((ug) -> ug.getUser())
    		.collect(Collectors.toList());
    	
    	return users;
    }
}
