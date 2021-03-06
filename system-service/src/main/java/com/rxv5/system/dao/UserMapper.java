package com.rxv5.system.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rxv5.system.model.User;

public interface UserMapper extends BaseMapper<User> {
	public List<User> findAll(User user);
}
