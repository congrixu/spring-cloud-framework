package com.rxv5.workflow.entity;

import java.io.Serializable;

public class Group implements Serializable {

    private static final long serialVersionUID = 7684301710245710322L;

    private String groupId;

    private String groupName;

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
