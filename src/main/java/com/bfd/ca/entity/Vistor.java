package com.bfd.ca.entity;

/**
 * 访客信息，记录访客ip 访问路径等信息
 * Created by jinwei.li on 2016/6/28.
 */
@SuppressWarnings("all")
public class Vistor {
    private String ip;
    private User user;
    private String path;
    private long time;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
