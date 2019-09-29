package com.rxv5.sso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import com.rxv5.sso.oauth2.AuthenticationFailHandler;
import com.rxv5.sso.oauth2.AuthenticationSuccessHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	/**
	 * 需要放行的URL
	 */
	private static final String[] AUTH_WHITELIST = {"/login" };

	@Autowired
	private AuthenticationSuccessHandler authentSuccessHandler;

	@Autowired
	private AuthenticationFailHandler authenticationFailHandler;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll();
		http.authorizeRequests().anyRequest().authenticated(); // 所有请求需要身份认证
		http.formLogin().permitAll().usernameParameter("username")
				.passwordParameter("password").successHandler(authentSuccessHandler)
				.failureHandler(authenticationFailHandler);
	}
}
