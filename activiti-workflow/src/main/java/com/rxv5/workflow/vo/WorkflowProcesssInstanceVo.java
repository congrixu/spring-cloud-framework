package com.rxv5.workflow.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程实例
 * 
 * @author rxv5
 *
 */
public class WorkflowProcesssInstanceVo implements Serializable {

    private static final long serialVersionUID = -1267122782979229261L;
    private String id;

    private String processDefinitionId;

    private String processInstanceId;

    private String applUserId;// 发启人

    private String applUserName;

    private String formName;

    private String formTitle;

    private String businessKey;

    private Date startDate;

    private Date endDate;

    private int state;
    private String stateStr;

    private int isSubProcess;

    private String isSubProcesStr;

    private String parentProcessId;// 主流程ID

    private int version;

    private String currentTaskName;

    private String currentTaskAssignee;

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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getApplUserId() {
        return applUserId;
    }

    public void setApplUserId(String applUserId) {
        this.applUserId = applUserId;
    }

    public String getApplUserName() {
        return applUserName;
    }

    public void setApplUserName(String applUserName) {
        this.applUserName = applUserName;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormTitle() {
        return formTitle;
    }

    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public int getIsSubProcess() {
        return isSubProcess;
    }

    public void setIsSubProcess(int isSubProcess) {
        this.isSubProcess = isSubProcess;
    }

    public String getIsSubProcesStr() {
        return isSubProcesStr;
    }

    public void setIsSubProcesStr(String isSubProcesStr) {
        this.isSubProcesStr = isSubProcesStr;
    }

    public String getParentProcessId() {
        return parentProcessId;
    }

    public void setParentProcessId(String parentProcessId) {
        this.parentProcessId = parentProcessId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCurrentTaskName() {
        return currentTaskName;
    }

    public void setCurrentTaskName(String currentTaskName) {
        this.currentTaskName = currentTaskName;
    }

    public String getCurrentTaskAssignee() {
        return currentTaskAssignee;
    }

    public void setCurrentTaskAssignee(String currentTaskAssignee) {
        this.currentTaskAssignee = currentTaskAssignee;
    }

}
