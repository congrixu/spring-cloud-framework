package com.rixuv5.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "T_USER")
public class User {

	@TableId(value = "ID", type = IdType.AUTO)
	private Integer id;//主键

	@TableField(value = "USER_NAME")
	private String userName;//登录名

	@TableField(value = "PASSWORD")
	private String password;//密码

	//真实姓名
	@TableField(value = "REAL_NAME")
	private String realName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

}
