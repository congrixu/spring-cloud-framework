package com.rxv5.auth.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

	private static final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

	/**
	 * 加密码密码
	 * @param pwd
	 * @return
	 */
	public static String encode(String pwd) {
		return bcrypt.encode(pwd);
	}

	/**
	 * 校验密码是否匹配
	 * @param rawPassword 非加密密码
	 * @param encodedPassword 加密密码
	 * @return
	 */
	public static boolean matches(String rawPassword, String encodedPassword) {
		return bcrypt.matches(rawPassword, encodedPassword);
	}
}
