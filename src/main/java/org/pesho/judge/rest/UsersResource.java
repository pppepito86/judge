package org.pesho.judge.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.model.User;

@Stateless
@Path("users")
public class UsersResource {
	
	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listUsers() {
    	TypedQuery<User> query = em.createNamedQuery("User.findAll", User.class);
    	List<User> results = query.getResultList();
    	return results;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User createUser(User user) {
    	em.persist(user);
    	return user;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") int userId) {
    	User user = em.find(User.class, (Integer)userId);
        return user;
    }
}
