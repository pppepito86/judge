package org.pesho.judge.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.pesho.judge.model.Role;
import org.pesho.judge.model.User;

@Stateless
public class UserDAO {

	@PersistenceContext(unitName = "judge")
	EntityManager em;

    public List<User> listUsers() {
    	TypedQuery<User> query = em.createNamedQuery("User.findAll", User.class);
    	List<User> results = query.getResultList();
    	return results;
    }
    
    public User createUser(User user) {
    	Role role = em.find(Role.class, user.getRoles().getId());
    	user.setRoles(role);
    	em.persist(user);
    	return user;
    }
    
    public User getUser(int userId) {
    	User user = em.find(User.class, (Integer)userId);
        return user;
    }
    
}