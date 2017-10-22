package org.pesho.judge.security;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

@Controller
public class JudgeUserDetailsService implements UserDetailsService {

	@Autowired
	private JdbcTemplate template;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Map<String, Object>> result = template.queryForList(
				"select username, passwordhash, rolename from users inner join roles on roleid = roles.id where username=?", 
				new Object[] {username}).stream().findFirst();
		
		return result.map(user -> new User(
				user.get("username").toString(), 
				user.get("passwordhash").toString(), 
				Arrays.asList(new SimpleGrantedAuthority(user.get("rolename").toString())))).orElse(null);
	}
	
}
