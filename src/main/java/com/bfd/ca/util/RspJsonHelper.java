package com.bfd.ca.util;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by jinwei.li on 2016/6/27.
 * 加工处理json返回结果
 */
@SuppressWarnings("all")
public class RspJsonHelper {
    private static RspJsonHelper instance = new RspJsonHelper();

    private RspJsonHelper() {

    }

    public static RspJsonHelper getInstance() {
        return instance;
    }

    /**
     * 成功信息
     * @return
     */
    public JSONObject getSuccessJson(){
        JSONObject json = new JSONObject();
        json.put("code",Constant.SUCCESS_CODE);
        return json;
    }

    /**
     * 失败信息
     * @return
     */
    public JSONObject getFailJson(){
        JSONObject json = new JSONObject();
        json.put("code",Constant.NAME_OR_PASSWORD_ERROR);
        return json;
    }

    /**
     * 登录失败信息
     * @return
     */
    public JSONObject getLoginFailJson(){
        JSONObject json = new JSONObject();
        json.put("code",Constant.NAME_OR_PASSWORD_ERROR);
        return json;
    }

    /**
     * 登录失败信息
     * @return
     */
    public JSONObject getSessionTimeOutJson(){
        JSONObject json = new JSONObject();
        json.put("code",Constant.SESSION_INVALID);
        return json;
    }
}
