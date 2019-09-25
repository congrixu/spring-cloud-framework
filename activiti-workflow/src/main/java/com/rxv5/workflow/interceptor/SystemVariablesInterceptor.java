package com.rxv5.workflow.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SystemVariablesInterceptor implements HandlerInterceptor{

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//System.out.println(request.getRequestURI());
		request.setAttribute("ctxPath", request.getContextPath());
		request.setAttribute("BUILD_TIME", new Date().getTime());
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	

}
