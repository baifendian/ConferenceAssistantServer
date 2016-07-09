package com.bfd.ca.entity;

/**
 * 错误信息
 * Created by jinwei.li on 2016/6/28.
 */
@SuppressWarnings("all")
public class ErrorMessage {
    private String reqPath;
    private String stackException;
    private long reqTime;

    public String getReqPath() {
        return reqPath;
    }

    public void setReqPath(String reqPath) {
        this.reqPath = reqPath;
    }

    public String getStackException() {
        return stackException;
    }

    public void setStackException(String stackException) {
        this.stackException = stackException;
    }

    public long getReqTime() {
        return reqTime;
    }

    public void setReqTime(long reqTime) {
        this.reqTime = reqTime;
    }
}
