package com.rxv5.workflow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rxv5.workflow.entity.User;

public interface UserMapper {

    public List<User> select(@Param("userId") String userId, @Param("userName") String userName);
}
