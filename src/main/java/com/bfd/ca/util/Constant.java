package com.bfd.ca.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jinwei.li on 2016/6/27.
 */
@SuppressWarnings("all")
public class Constant {
    //访问成功
    public static final int SUCCESS_CODE = 200;
    //用户名或密码错误
    public static final int NAME_OR_PASSWORD_ERROR = 501;
    //session过期
    public static final int SESSION_INVALID = 502;

    //未知错误
    public static final int ERROR_CODE = 599;
    public static final String WEBSOCKET_USER_NAME = "websocket_user_name";
    public static final String WEBSOCKET_TYPE_PUSH = "push";
    public static final String WEBSOCKET_TYPE_CHAT = "chat";
    public static final int SESSION_TIMEOUT = 30 * 24 * 60 * 60;
    public static String WEBSOCKET_TMP_FILE = "E:\\tmp\\";
    
    public static Map<String,String> MEETING_MESSAGE_STATUS = new ConcurrentHashMap<String, String>();
}
