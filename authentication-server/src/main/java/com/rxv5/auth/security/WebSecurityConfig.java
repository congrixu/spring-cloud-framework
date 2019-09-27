package com.rxv5.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.rxv5.auth.filter.JWTAuthenticationFilter;
import com.rxv5.auth.filter.JwtUsernamePasswordAuthenticationFilter;
import com.rxv5.auth.handler.Http401AuthenticationEntryPoint;
import com.rxv5.auth.service.CustomAuthenticationProvider;
import com.rxv5.auth.service.UserServiceApi;
import com.rxv5.auth.util.JwtTokenManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	/**
	 * 需要放行的URL
	 */
	private static final String[] AUTH_WHITELIST = { "/users/signup" };

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserServiceApi userService;

	@Autowired
	private JwtTokenManager jwtTokenManager;

	// 该方法是登录的时候会进入
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 使用自定义身份验证组件
		auth.authenticationProvider(new CustomAuthenticationProvider(userDetailsService));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll();
		http.authorizeRequests().anyRequest().authenticated(); // 所有请求需要身份认证
		http.exceptionHandling().authenticationEntryPoint(new Http401AuthenticationEntryPoint("Basic realm=\"MyApp\""));
		http.addFilter(
				new JwtUsernamePasswordAuthenticationFilter(authenticationManager(), userService, jwtTokenManager));
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtTokenManager));
		http.formLogin().loginPage("").loginProcessingUrl("");
		// 默认注销行为为logout，可以通过下面的方式来修改
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/login").permitAll();// 设置注销成功后跳转页面，默认是跳转到登录页面;
	}

}
