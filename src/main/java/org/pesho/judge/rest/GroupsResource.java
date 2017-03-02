package org.pesho.judge.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.model.Group;
import org.pesho.judge.model.User;
import org.pesho.judge.model.UserGroup;

@Stateless
@Path("groups")
public class GroupsResource {

	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Group> listGroups() {
        TypedQuery<Group> query = em.createNamedQuery("Group.findAll", Group.class);
    	List<Group> results = query.getResultList();
        return results;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Group createGroup(Group group) {
    	// TODO;
        return null;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Group getGroup(@PathParam("id") int groupId) {
    	Group group = em.find(Group.class, (Integer)groupId);
        return group;
    }
    
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Group updateGroup(@PathParam("id") int groupId) {
    	// TODO
        return null;
    }
    
    @GET
    @Path("{id}/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsersFromGroup(@PathParam("id") int groupId) {
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
