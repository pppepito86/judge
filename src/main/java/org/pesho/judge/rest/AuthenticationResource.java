package org.pesho.judge.rest;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.resource.spi.IllegalStateException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.pesho.judge.dto.TokenDTO;
import org.pesho.judge.model.User;
import org.pesho.judge.security.TokenGenerator;

@Path("authentication")
public class AuthenticationResource {

	@PersistenceContext(unitName = "judge")
	EntityManager em;

	@PermitAll
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public TokenDTO login(@FormParam("username") String username, @FormParam("password") String password) throws IllegalStateException {
		if (username == null || password == null) {
			throw new IllegalStateException("Username and password cannot be empty");
		}
		
		// TODO check password
		User user = (User) em.createNamedQuery("User.findByName").setParameter("name", username)
	              .getSingleResult();
		
		if(user == null) {
			throw new IllegalStateException("Invalid username or password");
		}
		
		String token = TokenGenerator.createToken(user.getId());
		
		TokenDTO tokenDto = new TokenDTO();
		tokenDto.setToken(token);
		
		return tokenDto;
	}

	@POST
	@Path("logout")
	public void logout(@Context ContainerRequestContext requestContext) {
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
			return;
		
		String token = authorizationHeader.substring("Bearer".length()).trim();
		
		TokenGenerator.deleteToken(token);
	}
	
}
