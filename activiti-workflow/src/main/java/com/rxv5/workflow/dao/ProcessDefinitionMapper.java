package com.rxv5.workflow.dao;

import java.util.List;

import com.rxv5.workflow.vo.ProcessDefinitionVo;

public interface ProcessDefinitionMapper {

	public List<ProcessDefinitionVo> select(ProcessDefinitionVo search);
}
