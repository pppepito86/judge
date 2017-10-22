package org.pesho.judge.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private JudgeUserDetailsService judgeUserDetailsService;

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(judgeUserDetailsService).passwordEncoder(passwordEncoder);
	}
	
	//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.csrf().disable().authorizeRequests()
		// .anyRequest().authenticated()
		// .and().httpBasic();
	     http
         .authorizeRequests()
         	.antMatchers("/api/**").authenticated()
             .antMatchers("/**").permitAll()
             .and()
             .httpBasic();		
	     http.csrf().disable();
		// http.authorizeRequests().antMatchers("/api/v1/login").authenticated();

		// http.authorizeRequests().antMatchers("/index.html", "/home.html",
		// "/login.html", "/").permitAll().anyRequest()
		// .authenticated();
		// http.httpBasic();
	}

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http.authorizeRequests()
	// .antMatchers("/api/**").permitAll();
	// .antMatchers("/**");
	/*
	 * .hasAuthority("admin") .antMatchers("/admin/**").hasAuthority("admin")
	 * .antMatchers("/assets/**").permitAll()
	 * .antMatchers("/login.html").permitAll() .anyRequest()
	 * .authenticated().and().csrf().disable().formLogin()
	 * .loginPage("/login.html").failureUrl("/login.html")
	 * .defaultSuccessUrl("/dashboard.html") .and().logout()
	 * .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	 * .logoutSuccessUrl("/").and().exceptionHandling()
	 * .accessDeniedPage("/access-denied");
	 */
	// }

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}
