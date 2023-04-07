package com.hemlock.www.backend.common;

public class JSONResult<T> {
    private String code;
    private String msg;
    private T data;

    public JSONResult() {
        this.msg = "success";
        this.code = "200";
    }

    public JSONResult(String code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public JSONResult (String code, String msg, T data) {
        this.msg = msg ;
        this.code = code;
        this.data = data;
    }

    public String getMsg () {
        return this.msg;
    }

    public String getCode() {
        return this.code;
    }

    public T getData() {
        return this.data;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }
}
