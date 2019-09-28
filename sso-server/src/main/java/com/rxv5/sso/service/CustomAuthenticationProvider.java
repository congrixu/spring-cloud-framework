package com.rxv5.sso.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

	/**
	* 是否可以提供输入类型的认证服务
	* @param authentication
	* @return
	*/
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
