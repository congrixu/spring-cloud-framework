package com.rxv5.workflow.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程定义
 * 
 * @author rxv5
 */
public class ProcessDefinitionVo implements Serializable {

    private static final long serialVersionUID = -1642139075044420874L;

    private String processDefinitionId;

    private String deploymentId;

    private String name;

    private String pdKey;

    private int version;

    private String resourceName;

    private String diagramResourceName;

    private Date deploymentTime;

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPdKey() {
        return pdKey;
    }

    public void setPdKey(String pdKey) {
        this.pdKey = pdKey;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDiagramResourceName() {
        return diagramResourceName;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public Date getDeploymentTime() {
        return deploymentTime;
    }

    public void setDeploymentTime(Date deploymentTime) {
        this.deploymentTime = deploymentTime;
    }

    @Override
    public String toString() {
        return "ProcessDefinitionVo [processDefinitionId=" + processDefinitionId
                + ", deploymentId=" + deploymentId + ", name=" + name
                + ", pdKey=" + pdKey + ", version=" + version
                + ", resourceName=" + resourceName + ", diagramResourceName="
                + diagramResourceName + ", deploymentTime=" + deploymentTime
                + "]";
    }

}
