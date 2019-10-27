package com.rxv5.workflow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rxv5.workflow.vo.CompletOperButtonVo;

public interface CompletOperButtonMapper {
    public List<CompletOperButtonVo> select(@Param("processDefinitionId") String processDefinitionId,
            @Param("bpmnId") String bpmnId);

    public void delete(@Param("processDefinitionId") String processDefinitionId, @Param("bpmnId") String bpmnId);

    public void insert(CompletOperButtonVo ob);
}
