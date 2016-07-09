package com.bfd.ca.service;

import com.bfd.ca.entity.Meeting;

/**
 * Created by jinwei.li on 2016/7/6.
 */
@SuppressWarnings("all")
public interface MeetingService {
    public Meeting getMeetingById(String meetingId);
}
