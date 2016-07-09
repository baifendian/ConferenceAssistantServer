package com.bfd.ca.controller;

import com.alibaba.fastjson.JSONObject;
import com.bfd.ca.entity.ErrorMessage;
import com.bfd.ca.util.LogUtil;
import com.bfd.ca.util.MongodbUtil;
import com.bfd.ca.util.PropertiesUtil;
import com.bfd.ca.util.RspJsonHelper;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("all")
public class BasicController {

    protected HttpServletRequest request;

    /**
     * 异常捕获处理
     *
     * @param ex
     * @param response
     * @param request
     */
    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception ex, HttpServletResponse response,
                                 HttpServletRequest request) {
        String reqPath = request.getServletPath();
        LogUtil.getLogger(ExceptionHandler.class).error("reqPath:\t" + reqPath, ex);
        //持久化异常信息
        boolean persist = PropertiesUtil.getBooleanValue("error");
        if (persist) {
            ErrorMessage msg = new ErrorMessage();
            msg.setReqTime(System.currentTimeMillis());
            msg.setReqPath(reqPath);
            msg.setStackException(ex.toString());
            MongodbUtil.getInstance().insert(MongodbUtil.MONGODB_DB, MongodbUtil.MONGODB_ERRORINFO, msg);
        }
        try {
            Writer writer = response.getWriter();
            JSONObject json = RspJsonHelper.getInstance().getFailJson();
            json.put("message","未知错误！");
            writer.append(json.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 数据格式
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
    }

    /**
     * 注入request
     *
     * @param request
     */
    @Resource
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }


}
