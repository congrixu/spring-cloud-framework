package com.rxv5.workflow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rxv5.workflow.vo.PDGroupConfigVo;

public interface PDConfigGroupMapper {
    public void deleteTaskGroupConfigByProcessDefinitionId(String pdId);

    public void save(PDGroupConfigVo groupConfig);

    public List<PDGroupConfigVo> select(@Param("processDefinitionId") String processDefinitionId,
            @Param("bpmnId") String bpmnId);
}
