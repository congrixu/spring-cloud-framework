package com.rxv5.auth.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rxv5.auth.service.UserServiceApi;
import com.rxv5.auth.util.JwtTokenManager;
import com.rxv5.auth.vo.SysVerifyVo;

/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法
 * attemptAuthentication ：接收并解析用户凭证。
 * successfulAuthentication ：用户成功登录后，这个方法会被调用，我们在这个方法里生成token。
 */
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private AuthenticationManager authenticationManager;

	private JwtTokenManager jwtTokenManager;

	private UserServiceApi userService;

	public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
			UserServiceApi userService, JwtTokenManager jwtTokenManager) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.jwtTokenManager = jwtTokenManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");

		logger.info("JwtUsernamePasswordAuthenticationFilter.userName:{},password:{}", userName, password);

		return authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(userName, password, new ArrayList<>()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		SysVerifyVo user = userService.findUserPrminssionsByName(auth.getPrincipal().toString());
		String token = jwtTokenManager.createToken(user);
		// 登录成功后，返回token到header里面
		response.addHeader("Authorization", "Bearer " + token);
	}
}
