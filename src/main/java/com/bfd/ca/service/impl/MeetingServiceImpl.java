package com.bfd.ca.service.impl;

import com.bfd.ca.entity.Meeting;
import com.bfd.ca.service.MeetingService;
import com.bfd.ca.util.MongodbUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jinwei.li on 2016/7/6.
 */
@Service("MeetingService")
@SuppressWarnings("all")
public class MeetingServiceImpl implements MeetingService {
    @Override
    public Meeting getMeetingById(String meetingId) {
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("uid", meetingId);
        List<Meeting> list = MongodbUtil.getInstance().getByCondition(MongodbUtil.MONGODB_DB, "meeting", condition, Meeting.class);
        if (list != null && list.size() > 0) {
            Meeting meeting = list.get(0);
            return meeting;
        } else
            return null;
    }
}
