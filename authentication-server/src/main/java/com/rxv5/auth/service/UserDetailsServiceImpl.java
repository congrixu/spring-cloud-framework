package com.rxv5.auth.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rxv5.auth.vo.SysVerifyVo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserServiceApi userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysVerifyVo user = userService.findUserPrminssionsByName(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		List<SimpleGrantedAuthority> roles = null;
		Set<String> roleSet = user.getRoles();
		if (roleSet != null && roleSet.size() > 0) {
			roles = roleSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		}
		return new org.springframework.security.core.userdetails.User(user.getLoginId(), user.getPasswd(), roles);
	}

}
