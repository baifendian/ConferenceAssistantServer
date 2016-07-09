package com.bfd.ca.entity;

import java.util.List;

/**
 * Created by jinwei.li on 2016/7/6.
 */
@SuppressWarnings("all")
public class Meeting {
    private String mid;
    private User user;
    private String title;
    private String content;
    private String addr;
    private long st;
    private long et;
    private List<User> plist;
    private String mtype;
    private String status;
    private String audit;
    private String image;
    private String remark;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public long getSt() {
        return st;
    }

    public void setSt(long st) {
        this.st = st;
    }

    public long getEt() {
        return et;
    }

    public void setEt(long et) {
        this.et = et;
    }

    public List<User> getPlist() {
        return plist;
    }

    public void setPlist(List<User> plist) {
        this.plist = plist;
    }

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
