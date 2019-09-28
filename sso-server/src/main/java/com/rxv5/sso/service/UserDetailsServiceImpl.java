package com.rxv5.sso.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*SysVerifyVo userVo = userService.findUserPrminssionsByName(username);
		if (userVo == null) {
			throw new UsernameNotFoundException(username);
		}
		
		List<SimpleGrantedAuthority> roles = null;
		Set<String> roleSet = userVo.getRoles();
		if (roleSet != null && roleSet.size() > 0) {
			roles = roleSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		}
		User user = new User(userVo.getLoginId(), userVo.getPasswd(), roles);*/
		List<SimpleGrantedAuthority> roles =Lists.newArrayList();
		User user = new User("admin", "$2a$10$SQTiXRpMRn0Wp4Hn2lc9iO7WzBz1DyShaBm.6OjFUV4NrymUerBn.", roles);
		return user;
	}

}
