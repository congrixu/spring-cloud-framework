package com.rixuv5.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rixuv5.system.dao.UserMapper;
import com.rixuv5.system.model.User;

@RestController
@RequestMapping("/system/user")
public class UserController {

	@Autowired
	private UserMapper userMapper;

	@RequestMapping("/get/{id}")
	public User get(@PathVariable("id") Integer id) {
		User result = this.userMapper.selectById(id);
		return result;
	}

	@RequestMapping("/find")
	public List<User> find() {
		List<User> users = userMapper.findAll(new User());
		return users;
	}
}
