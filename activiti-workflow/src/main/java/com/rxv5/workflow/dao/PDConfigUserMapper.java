package com.rxv5.workflow.dao;

import com.rxv5.workflow.vo.PDUserConfigVo;

public interface PDConfigUserMapper {

    public void deleteTaskUserConfigByProcessDefinitionId(String pdId);

    public void save(PDUserConfigVo userConfig);
}
