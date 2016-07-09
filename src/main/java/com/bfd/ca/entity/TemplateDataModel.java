package com.bfd.ca.entity;

import java.util.List;
@SuppressWarnings("all")
public class TemplateDataModel {
	private String fromUserName;

	private String toUserName;
	
	private String startMeetingTime;
	
	private String endMeetingTime;
	
	private String meetingAddr;
	
	private String detail;
	
	private String content;
	
	private String title;
	
	private String remark;
	
	private List<String> imgList;

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}


	public String getMeetingAddr() {
		return meetingAddr;
	}

	public void setMeetingAddr(String meetingAddr) {
		this.meetingAddr = meetingAddr;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}
	
	public String getStartMeetingTime() {
		return startMeetingTime;
	}

	public void setStartMeetingTime(String startMeetingTime) {
		this.startMeetingTime = startMeetingTime;
	}

	public String getEndMeetingTime() {
		return endMeetingTime;
	}

	public void setEndMeetingTime(String endMeetingTime) {
		this.endMeetingTime = endMeetingTime;
	}
}
