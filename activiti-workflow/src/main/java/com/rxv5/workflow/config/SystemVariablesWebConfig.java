package com.rxv5.workflow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rxv5.workflow.interceptor.SystemVariablesInterceptor;

@Configuration
public class SystemVariablesWebConfig implements WebMvcConfigurer {
	 @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        // addPathPatterns - 用于添加拦截规则
	        // excludePathPatterns - 用户排除拦截
	        registry.addInterceptor(new SystemVariablesInterceptor()).excludePathPatterns("*.js","*.css");
	    }
}
