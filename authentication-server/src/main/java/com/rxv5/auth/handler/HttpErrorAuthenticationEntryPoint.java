package com.rxv5.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class HttpErrorAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final String headerValue;
	private final int errorCode;//HttpServletResponse.SC_UNAUTHORIZED

	public HttpErrorAuthenticationEntryPoint(String headerValue, int errorCode) {
		this.headerValue = headerValue;
		this.errorCode = errorCode;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setHeader("Authorization", this.headerValue);
		response.sendError(errorCode, authException.getMessage());
	}

}
