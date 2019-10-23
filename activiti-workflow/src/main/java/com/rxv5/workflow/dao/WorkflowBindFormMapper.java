package com.rxv5.workflow.dao;

import java.util.List;

import com.rxv5.workflow.vo.WorkflowBindFormVo;

public interface WorkflowBindFormMapper {

    public List<WorkflowBindFormVo> select(String pdKey);

    public void insert(WorkflowBindFormVo bindForm);

    public void delete(Integer id);
}
