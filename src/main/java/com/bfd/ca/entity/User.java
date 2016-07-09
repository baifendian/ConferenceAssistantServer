package com.bfd.ca.entity;

import java.io.Serializable;

/**
 * Created by jinwei.li on 2016/6/27.
 */
@SuppressWarnings("all")
public class User implements Serializable {
    //用户id
    private String uid;
    //用户名
    private String name;
    //密码
    private String password;
    //邮箱
    private String email;
    //电话
    private String phone;
    //图片url
    private String imgurl;
    //qq号码
    private String qq;
    //创建时间
    private long ct;
    //修改时间
    private long ut;
    //用户状态0：已锁定 1：激活
    private int status;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public long getCt() {
        return ct;
    }

    public void setCt(long ct) {
        this.ct = ct;
    }

    public long getUt() {
        return ut;
    }

    public void setUt(long ut) {
        this.ut = ut;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
