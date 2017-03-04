package org.pesho.judge.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import org.pesho.judge.model.User;

public class JudgeSecurityContext implements SecurityContext {

	private Principal principal;
	//private Role role;

	public JudgeSecurityContext(User user) {
		principal = new JudgePrincipal(user.getUsername());
		//this.role = user.getRoles();
	}

	@Override
	public Principal getUserPrincipal() {
		return principal;
	}

	@Override
	public boolean isUserInRole(String role) {
		return true;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public String getAuthenticationScheme() {
		return null;
	}

}
