package com.rxv5.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rxv5.system.service.UserApiService;
import com.rxv5.system.vo.UserVo;
import com.rxv5.vo.Page;

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

	@RequestMapping("/query")
	public Page<UserVo> query(@RequestBody(required = false) UserVo search, @RequestParam("pageNum") Integer pageNum,
			@RequestParam("pageSize") Integer pageSize) {
		return userApiService.query(new UserVo(), pageNum, pageSize);
	}
}
