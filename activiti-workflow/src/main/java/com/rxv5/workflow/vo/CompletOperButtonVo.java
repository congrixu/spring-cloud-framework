package com.rxv5.workflow.vo;

import java.io.Serializable;

/**
 * 操作按钮设置
 */
public class CompletOperButtonVo implements Serializable {

    private static final long serialVersionUID = 8523614433344813032L;

    private String id;

    private String processDefinitionId;

    private String bpmnId;

    private String operTitle;

    private String operValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getOperTitle() {
        return operTitle;
    }

    public void setOperTitle(String operTitle) {
        this.operTitle = operTitle;
    }

    public String getOperValue() {
        return operValue;
    }

    public void setOperValue(String operValue) {
        this.operValue = operValue;
    }

}
