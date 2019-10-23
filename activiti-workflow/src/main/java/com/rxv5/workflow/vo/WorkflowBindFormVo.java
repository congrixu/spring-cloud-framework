package com.rxv5.workflow.vo;

import java.io.Serializable;

public class WorkflowBindFormVo implements Serializable {

    private static final long serialVersionUID = -8120374739776101299L;

    private String id;
    private String formName;
    private String processDefinitionKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

}
