package com.rxv5.workflow.dao;

import com.rxv5.workflow.vo.PDGroupConfigVo;

public interface PDConfigGroupMapper {
    public void deleteTaskGroupConfigByProcessDefinitionId(String pdId);

    public void save(PDGroupConfigVo groupConfig);
}
