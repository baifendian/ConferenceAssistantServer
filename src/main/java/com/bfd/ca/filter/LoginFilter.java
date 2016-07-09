package com.bfd.ca.filter;

import com.alibaba.fastjson.JSONObject;
import com.bfd.ca.entity.User;
import com.bfd.ca.util.LogUtil;
import com.bfd.ca.util.RedisHelper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created by jinwei.li on 2016/6/27.
 */
@SuppressWarnings("all")
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        String token = request.getHeader("token");
        String reqPath = request.getServletPath();
        if(reqPath.startsWith("/static")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        User u = null;
        if (token != null) {
            String userStr = RedisHelper.get(token);
            if (userStr != null) {
                JSONObject userJson = JSONObject.parseObject(userStr);
                try {
                    u = new User();
                    BeanUtils.copyProperties(u, new HashMap(userJson));
                    HttpSession session = request.getSession();
                    session.setAttribute("user", u);
                    session.setAttribute("meetingId", "123");
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LogUtil.getLogger(LoginFilter.class).error(e);
                    e.printStackTrace();
                }
            }
        } else {
            HttpSession session = request.getSession();
            //获取session中用户信息
            u = (User) session.getAttribute("user");
        }
        if (u == null && !reqPath.equals("/user/login")) {
            request.getRequestDispatcher("/user/verificationFail").forward(servletRequest, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
