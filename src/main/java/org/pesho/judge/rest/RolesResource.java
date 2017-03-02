package org.pesho.judge.rest;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.pesho.judge.model.Role;

@Stateless
@Path("roles")
public class RolesResource {

	@PersistenceContext(unitName = "judge")
	EntityManager em;
	
	// TODO: shall not be visible outside
	
    @POST
    public Response createRoles(Role role) {
    	em.persist(role);
    	return Response.status(Response.Status.CREATED).build();
    }
    
}