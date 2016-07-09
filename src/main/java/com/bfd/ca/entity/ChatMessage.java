package com.bfd.ca.entity;

/**
 * 消息主体
 * Created by jinwei.li on 2016/7/1.
 */
@SuppressWarnings("all")
public class ChatMessage {
    private String name;
    private String meetingId;
    private String content;
    private String file;
    private long ct;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public long getCt() {
        return ct;
    }

    public void setCt(long ct) {
        this.ct = ct;
    }
}
