package com.rxv5.auth.service;

import java.util.Set;

import com.rxv5.auth.vo.SysVerifyVo;

public interface UserServiceApi {
	/**
	 * 根据登录id获取用户信息
	 * @param loginId
	 * @return
	 */
	public SysVerifyVo findByName(String loginId);

	/**
	 * 根据登录id获取用户权限和角色
	 * @param loginId
	 * @return
	 */
	public SysVerifyVo findUserPrminssionsByName(String loginId);

	/**
	 * 根据登录id获取权限
	 * @param loginId
	 * @return
	 */
	public Set<String> findPermissions(String loginId);

}
