package org.pesho.judge.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private JudgeUserDetailsService judgeUserDetailsService;
	
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username,passwordhash,'true' from users where username=?")
				.authoritiesByUsernameQuery("select username, rolename from users where username=?")
				.passwordEncoder(passwordEncoder);
				*/
		auth.userDetailsService(judgeUserDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/api/**").hasAuthority("admin")
		.antMatchers("/**").permitAll()
		.anyRequest().authenticated().and().csrf().disable().formLogin()
		.loginPage("/login.html").failureUrl("/login.html");
		//.antMatchers("/**").permitAll();
		/*.antMatchers("/").permitAll()
		.antMatchers("/assets/**").permitAll()
		.antMatchers("/login.html").permitAll()
		.antMatchers("/admin/**").hasAuthority("admin").anyRequest()
		.authenticated().and().csrf().disable().formLogin()
		.loginPage("/login.html").failureUrl("/login?error=true")
		.defaultSuccessUrl("/admin/users")
		.and().logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/").and().exceptionHandling()
		.accessDeniedPage("/access-denied");
		*/
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}
