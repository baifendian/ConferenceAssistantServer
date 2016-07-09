package com.bfd.ca.controller;

import com.alibaba.fastjson.JSONObject;
import com.bfd.ca.entity.User;
import com.bfd.ca.service.UserService;
import com.bfd.ca.util.*;
import com.mongodb.BasicDBObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by jinwei.li on 2016/6/27.
 */
@Controller
@RequestMapping("user")
@SuppressWarnings("all")
public class UserController extends BasicController {
    @Resource
    private UserService userService;

    @RequestMapping("/web/login")
    @ResponseBody
    public String login(@RequestParam String name, @RequestParam String password, String verification) {
        JSONObject result;
        HttpSession session = request.getSession();
        String _verification = (String) session.getAttribute("bfd_123");
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("name", name);
        condition.put("password", DigestUtils.md5Hex(name + password));
        //判断用户是否有效
        User user = userService.getUser(condition);
        if (user != null) {
            result = RspJsonHelper.getInstance().getSuccessJson();
            result.put("message", I18n.getInstance().getMessage("login_success", name));
            JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(user));
            String token = UUID.randomUUID().toString();
            data.put("token", token);
            //设置登录超时时间10分钟
            RedisHelper.setEx(token, data.toJSONString(), Constant.SESSION_TIMEOUT);
            result.put("data", data);
        } else {
            result = RspJsonHelper.getInstance().getLoginFailJson();
            result.put("message", I18n.getInstance().getMessage("login_fail"));
        }
        return result.toJSONString();
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(@RequestParam String name, @RequestParam String password) {
        JSONObject result;
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("name", name);
        condition.put("password", DigestUtils.md5Hex(name + password));
        //判断用户是否有效
        User user = userService.getUser(condition);
        if (user != null) {
            result = RspJsonHelper.getInstance().getSuccessJson();
            result.put("message", I18n.getInstance().getMessage("login_success", name));
            JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(user));
            String token = UUID.randomUUID().toString();
            data.put("token", token);
            //设置登录超时时间10分钟
            RedisHelper.setEx(token, data.toJSONString(), Constant.SESSION_TIMEOUT);
            result.put("data", data);
        } else {
            result = RspJsonHelper.getInstance().getLoginFailJson();
            result.put("message", I18n.getInstance().getMessage("login_fail"));
        }
        return result.toJSONString();
    }

    @RequestMapping("verification")
    public void verification(HttpServletRequest request, HttpServletResponse response) {
        RandomValidateCode.getInstance().getRandcode(request, response);
        try {
            response.getOutputStream().close();
        } catch (IOException e) {
            LogUtil.getLogger(UserController.class).error(e);
            e.printStackTrace();
        }
    }

    @RequestMapping("addUser")
    @ResponseBody
    public String addUser(User user) {
        JSONObject result;
        user.setUid(UUID.randomUUID().toString());
        String name = user.getName();
        String password = user.getPassword();
        user.setPassword(DigestUtils.md5Hex(name + password));
        user.setCt(System.currentTimeMillis());
        //增加用户
        boolean available = userService.addUser(user);
        if (available) {
            result = RspJsonHelper.getInstance().getSuccessJson();
        } else {
            result = RspJsonHelper.getInstance().getLoginFailJson();
        }
        return result.toJSONString();
    }

    @RequestMapping("updateUser")
    @ResponseBody
    public String updateUser(String uid, User user) {
        JSONObject result;
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("uid", uid);
        String name = user.getName();
        String password = user.getPassword();
        user.setPassword(DigestUtils.md5Hex(name + password));
        //修改用户
        boolean available = userService.updateUser(condition, user);
        if (available) {
            result = RspJsonHelper.getInstance().getSuccessJson();
        } else {
            result = RspJsonHelper.getInstance().getLoginFailJson();
        }
        return result.toJSONString();
    }

    @RequestMapping("getAllUser")
    @ResponseBody
    public String getAllUser() {
        JSONObject result;
        HashMap<String, Object> condition = new HashMap<>();
        //获取所有用户
        List<User> list = userService.getAllUser(condition);
        result = RspJsonHelper.getInstance().getSuccessJson();
        result.put("data", list);
        return result.toJSONString();
    }


    /**
     * session验证失败，提示登录
     *
     * @return
     */
    @RequestMapping("verificationFail")
    @ResponseBody
    public String verificationFail() {
        JSONObject result;
        result = RspJsonHelper.getInstance().getSessionTimeOutJson();
        result.put("message", I18n.getInstance().getMessage("session_invalid"));
        return result.toJSONString();
    }

    @ResponseBody
    @RequestMapping("/me")
    public String queryMyInfo(@RequestParam String uid) {
        JSONObject result;
        BasicDBObject object = new BasicDBObject();
        object.put("uid", uid);
        List<Document> list = CaMongodbUtil.queryData("user", object);
        if (list.size() == 0) {
            result = RspJsonHelper.getInstance().getSuccessJson();
            result.put("data", list);
            return result.toJSONString();
        }

        result = RspJsonHelper.getInstance().getSuccessJson();
        Document document = list.get(0);

        //查询"我"发起的
        object = new BasicDBObject();
        object.put("user.uid", uid);
        long publishFromMe = CaMongodbUtil.countData("meeting", object);
        document.put("publishFromMeCount", publishFromMe);

        //查询"我"参与的
        object = new BasicDBObject();
        object.put("plist.uid", uid);
        long participateCount = CaMongodbUtil.countData("meeting", object);
        document.put("participateCount", participateCount);

        //查询和"我"相关的待办事项
        object = new BasicDBObject();
        object.put("plist.uid", uid);
        long todolistCount = CaMongodbUtil.countData("todolist", object);
        document.put("todolistCount", todolistCount);

        result.put("data", document);
        return result.toJSONString();
    }
}
