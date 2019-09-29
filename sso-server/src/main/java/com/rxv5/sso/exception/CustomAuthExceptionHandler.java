package com.rxv5.sso.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setContentType("application/json;charset=UTF-8");
		        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        response.addHeader("Access-Control-Allow-Origin", "*");
		        response.addHeader("Cache-Control","no-cache");
		        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		        response.addHeader("Access-Control-Max-Age", "1800");
		        //访问资源的用户权限不足
		        logger.error("AccessDeniedException : {}",accessDeniedException.getMessage());
		        response.getWriter().write("访问资源的用户权限不足");

	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		Throwable cause = authException.getCause();
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		response.addHeader("Access-Control-Max-Age", "1800");
		if (cause instanceof InvalidTokenException) {
			logger.error("InvalidTokenException : {}", cause.getMessage());
			// Token无效
			response.getWriter().write("无效的token");
		} else {
			logger.error("AuthenticationException : NoAuthentication");
			// 资源未授权
			response.getWriter().write("资源未授权");
		}

	}

}
