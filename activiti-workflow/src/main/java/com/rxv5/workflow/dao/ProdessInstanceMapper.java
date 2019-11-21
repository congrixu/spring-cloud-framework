package com.rxv5.workflow.dao;

import java.util.List;

import com.rxv5.workflow.vo.WorkflowProcesssInstanceVo;

public interface ProdessInstanceMapper {

    public List<WorkflowProcesssInstanceVo> select(WorkflowProcesssInstanceVo search);
}
