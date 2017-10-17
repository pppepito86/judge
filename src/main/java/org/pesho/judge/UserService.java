package org.pesho.judge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class UserService {

	@Autowired
	private JdbcTemplate template;
	
	public Long getCurrentUserId() {
		String name = getCurrentUserName();
		return getUserId(name);
	}
	
	public String getCurrentUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}
	
	public String getCurrentUserRole() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getAuthorities().stream().map(auths -> auths.getAuthority()).findFirst().orElse("guest");
	}
	
	public Long getUserId(String username) {
		return template.queryForObject("select id from users where username=?", 
				new Object[] {username}, Long.class);
	}
	
}
