package com.rxv5.workflow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rxv5.workflow.vo.PDUserConfigVo;

public interface PDConfigUserMapper {

    public void deleteTaskUserConfigByProcessDefinitionId(String pdId);

    public void save(PDUserConfigVo userConfig);

    public List<PDUserConfigVo> select(@Param("processDefinitionId") String processDefinitionId,
            @Param("bpmnId") String bpmnId);
}
