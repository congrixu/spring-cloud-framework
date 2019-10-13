package com.rxv5.vo;

import java.io.Serializable;
import java.util.List;

public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = -6231827614631647711L;

    private boolean success;
    private List<T> data;
    private String message;

    public JsonResult() {
        super();
    }

    public JsonResult(List<T> data) {
        this.success = true;// 如果有返回值 ，默认是成功的
        this.data = data;
    }

    public JsonResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
