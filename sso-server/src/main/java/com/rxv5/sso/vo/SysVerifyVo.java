package com.rxv5.sso.vo;

import java.util.Set;

public class SysVerifyVo {
	/**
	* 登录字符串
	*/
	private String loginId;
	/**
	 * 登录密码
	 */
	private String passwd;
	
	private String openid;

	//角色
	private Set<String> roles;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	

}
