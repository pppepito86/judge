package org.pesho.judge.rest;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.pesho.judge.model.Role;

@Path("roles")
public class RolesResource {
	
	@Context UriInfo uiField;
	
	@PersistenceContext(unitName="judge")
    private EntityManager entityManager;
	

	@PersistenceUnit
    EntityManagerFactory entityManagerFactory;
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Role> listRoles() {
        return null;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Role createRole(Role role) {
    	System.out.println(uiField);
    	System.out.println(entityManager);
    	System.out.println(entityManagerFactory);
    	try {
    		//entityManager.persist(role);
    	} catch (Throwable t) {
    		t.printStackTrace();
    	}
        return role;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Role getRole(@PathParam("id") int roleId) {
        return null;
    }
    
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Role updateRole(@PathParam("id") int roleId) {
        return null;
    }
}
