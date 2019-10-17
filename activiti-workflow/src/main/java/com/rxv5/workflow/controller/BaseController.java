package com.rxv5.workflow.controller;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rxv5.workflow.util.StrKit;
import com.rxv5.workflow.util.TypeConverter;

public class BaseController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String name, Class<T> clazz) {

		Object bean = createInstance(clazz);
		String beanNameAndDot = StringUtils.isNotBlank(name) ? name + "." : null;
		Map<String, String[]> parasMap = request.getParameterMap();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("set") == false || methodName.length() <= 3) { // only setter method
				continue;
			}
			Class<?>[] types = method.getParameterTypes();
			if (types.length != 1) { // only one parameter
				continue;
			}

			String attrName = StrKit.firstCharToLowerCase(methodName.substring(3));
			String paraName = beanNameAndDot != null ? beanNameAndDot + attrName : attrName;
			String paraNameOper = name != null ? name + "[" + attrName + "]" : attrName;
			if (parasMap.containsKey(paraName) || parasMap.containsKey(paraNameOper)) {
				try {
					String paraValue = request.getParameter(paraName);
					if (paraValue == null)
						paraValue = request.getParameter(paraNameOper);
					Object value = paraValue != null ? TypeConverter.convert(types[0], paraValue) : null;
					method.invoke(bean, value);
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				}
			}
		}

		return (T) bean;
	}

	private static <T> T createInstance(Class<T> objClass) {
		try {
			return objClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
