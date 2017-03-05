package org.pesho.judge.security;

import java.io.IOException;
import java.util.List;

import javax.annotation.Priority;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.pesho.judge.model.User;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	@PersistenceContext(unitName = "judge")
	EntityManager em;

	@Context
	HttpHeaders headers;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
		if (authHeaders == null || authHeaders.isEmpty()) {
			return;
		}

		String token = authHeaders.get(0).substring("Bearer".length()).trim();
		Integer userId = TokenGenerator.getUser(token);

		if (userId == null) {
			return;
		}

		User user = em.find(User.class, userId);
		requestContext.setSecurityContext(new JudgeSecurityContext(user));
	}

}