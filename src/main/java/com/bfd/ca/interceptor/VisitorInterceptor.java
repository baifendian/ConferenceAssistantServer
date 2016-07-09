package com.bfd.ca.interceptor;

import com.alibaba.fastjson.JSON;
import com.bfd.ca.entity.User;
import com.bfd.ca.entity.Vistor;
import com.bfd.ca.util.MongodbUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 访客记录
 */
public class VisitorInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
        //记录访客信息
        Vistor vistor = new Vistor();
        String ip = request.getRemoteHost();
        String path = request.getServletPath();
        if(path.startsWith("/static")){
            return;
        }
        User user = (User) request.getSession().getAttribute("user");
        vistor.setIp(ip);
        vistor.setPath(path);
        vistor.setUser(JSON.toJSONString(user));
        vistor.setTime(System.currentTimeMillis());
        //记录访客信息
        MongodbUtil.getInstance().insert(MongodbUtil.MONGODB_DB, MongodbUtil.MONGODB_VISTORINFO, vistor);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return super.preHandle(request, response, handler);
    }

}
