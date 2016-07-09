package com.bfd.ca.service.websocket;

import com.alibaba.fastjson.JSONObject;
import com.bfd.ca.entity.Meeting;
import com.bfd.ca.entity.User;
import com.bfd.ca.service.MeetingService;
import com.bfd.ca.util.Constant;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
@SuppressWarnings("all")
public class MyHandler extends AbstractWebSocketHandler {
    @Resource
    private MeetingService meetingService;

    private static final ArrayList<WebSocketSession> users = new ArrayList<>();
    //延迟消息队列，key为用户名
    private static final ConcurrentHashMap<String, ConcurrentLinkedQueue<WebSocketMessage>> delayMsgQueue = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        users.add(session);
        //获取当前用户用户名
        Map<String, Object> attribute = session.getAttributes();
        String username = (String) attribute.get(Constant.WEBSOCKET_USER_NAME);
        String meetingId = (String) attribute.get("meetingId");
        if (username == null)
            return;
        //如果未收到消息，登录时推送
        if (delayMsgQueue.contains(username + "$" + meetingId)) {
            ConcurrentLinkedQueue<WebSocketMessage> queue = delayMsgQueue.get(username);
            while (!queue.isEmpty()) {
                session.sendMessage(queue.poll());
            }
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        Map<String, Object> attribute = session.getAttributes();
        String username = (String) attribute.get(Constant.WEBSOCKET_USER_NAME);
        if (username == null)
            return;
        String msg = message.getPayload();
        JSONObject json = JSONObject.parseObject(msg);
        String type = json.getString("type");
        String meetingId = json.getString("mid");
                //用于获取会议参与人
        Meeting meeting = meetingService.getMeetingById(meetingId);
        if (meeting == null)
            return;
        List<String> include = new ArrayList<>();
        List<User> us = meeting.getPlist();
        for (User u : us) {
            include.add(u.getName()+"$"+meetingId);
        }
        List<String> exclude = Lists.newArrayList(username);
        //发送推送
        sendMessageToUsers(message, include, exclude);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        Map<String, Object> attribute = session.getAttributes();
        String username = (String) attribute.get(Constant.WEBSOCKET_USER_NAME);
        String meetingId = (String) attribute.get("meetingId");
        //获取会议信息
        Meeting meeting = meetingService.getMeetingById(meetingId);
        if (meeting == null)
            return;
        List<String> include = new ArrayList<>();
        List<User> us = meeting.getPlist();
        for (User u : us) {
            include.add(u.getName()+"$"+meetingId);
        }
        List<String> exclude = Lists.newArrayList(username);
        ByteBuffer byteBuffer = message.getPayload();
        String fileName = UUID.randomUUID().toString() + ".wav";
        Files.write(byteBuffer.array(), new File(Constant.WEBSOCKET_TMP_FILE + fileName));
        JSONObject result = new JSONObject();
        result.put("name", username);
        result.put("file", fileName);
        //发送推送
        sendMessageToUsers(new TextMessage(result.toJSONString()), include, exclude);
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(WebSocketMessage message, List<String> include, List<String> exclude) {
        for (String userAndMeeting : include) {
            String[] userAndMeetings = userAndMeeting.split("$");
            String name = userAndMeetings[0];
            String meeting = userAndMeetings[1];
            //未在线用户
            boolean b = true;
            for (WebSocketSession user : users) {
                if (user.isOpen()) {
                    try {
                        Map<String, Object> attribute = user.getAttributes();
                        String username = (String) attribute.get(Constant.WEBSOCKET_USER_NAME);
                        if (name.equals(username)) {
                            user.sendMessage(message);
                            b = false;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //如果未找到对应的用户，添加到延迟队列
            if (b) {
                if (delayMsgQueue.contains(userAndMeeting)) {
                    ConcurrentLinkedQueue<WebSocketMessage> queue = delayMsgQueue.get(userAndMeeting);
                    queue.add(message);
                } else {
                    ConcurrentLinkedQueue<WebSocketMessage> queue = new ConcurrentLinkedQueue<>();
                    queue.add(message);
                    delayMsgQueue.put(userAndMeeting, queue);
                }
            }
        }
    }
}