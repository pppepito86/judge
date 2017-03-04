package org.pesho.judge.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.model.Role;

@Stateless
@Path("roles")
public class RolesResource {

	@PersistenceContext(unitName = "judge")
	EntityManager em;

	// TODO: shall not be visible outside

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Role createRoles(Role role) {
		try {
			em.persist(role);
			return role;
		} catch (Throwable t) {
			return role;
		}
	}

}