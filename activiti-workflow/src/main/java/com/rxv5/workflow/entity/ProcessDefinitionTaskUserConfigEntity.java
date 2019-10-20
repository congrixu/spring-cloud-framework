package com.rxv5.workflow.entity;

import java.io.Serializable;

/**
 * 流程定义用户任务设置办理人配置
 *
 */
public class ProcessDefinitionTaskUserConfigEntity implements Serializable {

    private static final long serialVersionUID = -4694537948419288850L;
    private Integer id;
    private String processDefinitionId;// 流程定义ID
    private String bpmnId;// 流程定义文件指定节点ID
    private String userId;// 用户id
    private String userName;// 用户名称

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ProcessDefinitionTaskUserConfigEntity [id=" + id + ", processDefinitionId=" + processDefinitionId
                + ", bpmnId=" + bpmnId + ", userId=" + userId + ", userName=" + userName + "]";
    }

}
