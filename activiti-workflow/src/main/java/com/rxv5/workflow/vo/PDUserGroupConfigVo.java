package com.rxv5.workflow.vo;

import java.io.Serializable;

/**
 * 流程定义用户任务办理人员&组信息VO
 *
 */
public class PDUserGroupConfigVo implements Serializable {

    private static final long serialVersionUID = 7193396714618560531L;

    private String bpmnId;
    private String userIds;
    private String userNames;
    private String groupIds;
    private String groupNames;

    public String getBpmnId() {
        return bpmnId;
    }

    public void setBpmnId(String bpmnId) {
        this.bpmnId = bpmnId;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getUserNames() {
        return userNames;
    }

    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

}
