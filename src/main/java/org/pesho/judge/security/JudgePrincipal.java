package org.pesho.judge.security;

import java.security.Principal;

public class JudgePrincipal implements Principal {

	private String name;
	
	public JudgePrincipal(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

}
