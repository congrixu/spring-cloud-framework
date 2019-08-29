package com.rixuv5.system.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rixuv5.system.model.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
