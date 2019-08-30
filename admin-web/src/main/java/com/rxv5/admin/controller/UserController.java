package com.rxv5.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rxv5.system.service.UserApiService;
import com.rxv5.system.vo.UserVo;

@RestController
@RequestMapping("/system/user")
public class UserController {
	@Autowired
	private UserApiService userApiService;

	@RequestMapping("/get/{id}")
	public UserVo get(@PathVariable("id") Integer id) {
		UserVo result = this.userApiService.get(id);
		return result;
	}

	@RequestMapping("/find")
	public List<UserVo> find() {
		List<UserVo> users = userApiService.find(new UserVo());
		return users;
	}
}
