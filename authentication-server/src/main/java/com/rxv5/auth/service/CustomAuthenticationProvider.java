package com.rxv5.auth.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rxv5.auth.util.PasswordUtil;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	private UserDetailsService userDetailsService;

	public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// 获取认证的用户名 & 密码
		String loginid = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();
		UserDetails userDetails = userDetailsService.loadUserByUsername(loginid);
		if (null != userDetails) {
			if (PasswordUtil.matches(password, userDetails.getPassword())) {
				Authentication auth = new UsernamePasswordAuthenticationToken(loginid, password,
						authentication.getAuthorities());
				return auth;
			} else {
				throw new BadCredentialsException("密码错误");
			}
		} else {
			throw new UsernameNotFoundException("用户不存在");
		}
	}

	/**
	* 是否可以提供输入类型的认证服务
	* @param authentication
	* @return
	*/
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
