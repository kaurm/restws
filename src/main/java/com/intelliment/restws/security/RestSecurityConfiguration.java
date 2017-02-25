/**
 * Defining rules for Basic Auth.
 */
package com.intelliment.restws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("mandy").password("abc123").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("test").password("abc123").roles("USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Below commented line can  be used if we want to restrict access to endpoints based on roles.
		//http.csrf().disable().authorizeRequests().antMatchers("/top/**").hasRole("ADMIN").antMatchers("/search").access("hasRole('USER')and hasRole('ADMIN')").anyRequest().authenticated().and().httpBasic();
		
		//this will authenticate each and every request.
		http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic();
	}
}
