package com.event.api.streaming.client.config;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${stream.username}")
	private String username;

	@Value("${stream.password}")
	private String password;
	
	@Value("${stream.server.ip}")
	private String streamServerIP;
	
	private static String authenticationHeader = null;

	public static WebClient webClient = null;
	
	@PostConstruct 
	void initilize() {
		webClient = WebClient.create(streamServerIP);
		authenticationHeader = "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes());
	}
	
	public static String getAuthenticatonHeader() {
		return authenticationHeader;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		.requestMatchers()
		.and()
		.authorizeRequests()
		.antMatchers("/listener/**").authenticated()
		.and()
		.httpBasic()
		.and()
		.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> authBuilder = auth
				.inMemoryAuthentication();
		authBuilder
		.withUser(this.username)
		.password("{noop}" + this.password)
		.roles("USER", "ADMIN");
	}
}