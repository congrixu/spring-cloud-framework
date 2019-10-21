package com.rxv5.workflow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rxv5.workflow.entity.Group;

public interface GroupMapper {

    public List<Group> select(@Param("groupId") String groupId, @Param("groupName") String groupName);
}
