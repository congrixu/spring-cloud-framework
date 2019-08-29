package com.rixuv5.system.dao;

import java.util.List;

import com.rixuv5.system.model.User;

public interface UserDao {

	public List<User> findAll(User user);
}
