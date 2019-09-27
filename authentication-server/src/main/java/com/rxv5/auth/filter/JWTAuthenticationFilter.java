package com.rxv5.auth.filter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rxv5.auth.exception.TokenException;
import com.rxv5.auth.util.JwtTokenManager;
import com.rxv5.auth.vo.SysVerifyVo;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	private JwtTokenManager jwtTokenManager;
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenManager = jwtTokenManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty()) {
			throw new TokenException("Token为空");
		} else {
			token = token.replace("Bearer ", "");
		}

		try {
			SysVerifyVo user = jwtTokenManager.getUserFromToken(token);
			List<SimpleGrantedAuthority> roles = null;
			Set<String> roleSet = user.getRoles();
			if (roleSet != null && roleSet.size() > 0) {
				roles = roleSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
			}
			return new UsernamePasswordAuthenticationToken(user.getLoginId(), user.getPasswd(), roles);
		} catch (ExpiredJwtException e) {
			logger.error("Token:{},已过期: {} ", token, e.getMessage());
			throw new TokenException("Token已过期");
		} catch (UnsupportedJwtException e) {
			logger.error("Token:{},格式错误: {} ", token, e.getMessage());
			throw new TokenException("Token格式错误");
		} catch (MalformedJwtException e) {
			logger.error("Token:{},没有被正确构造: {} ", token, e.getMessage());
			throw new TokenException("Token没有被正确构造");
		} catch (SignatureException e) {
			logger.error("Token:{},签名失败: {} ", token, e.getMessage());
			throw new TokenException("签名失败");
		} catch (IllegalArgumentException e) {
			logger.error("Token:{},非法参数异常: {} ", token, e.getMessage());
			throw new TokenException("非法参数异常");
		}
	}

}
