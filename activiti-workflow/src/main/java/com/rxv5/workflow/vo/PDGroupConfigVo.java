package com.rxv5.workflow.vo;

import java.io.Serializable;

public class PDGroupConfigVo implements Serializable {

    private static final long serialVersionUID = 205519770784477926L;
    private Integer id;
    private String processDefinitionId;// 流程定义ID
    private String bpmnId;// 流程定义文件指定节点ID
    private String groupId;// 用户组id
    private String groupName;// 用户组名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getBpmnId() {
        return bpmnId;
    }

    public void setBpmnId(String bpmnId) {
        this.bpmnId = bpmnId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
