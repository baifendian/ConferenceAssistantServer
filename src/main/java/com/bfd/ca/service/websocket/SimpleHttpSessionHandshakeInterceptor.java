package com.bfd.ca.service.websocket;

import com.bfd.ca.entity.User;
import com.bfd.ca.util.Constant;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by jinwei.li on 2016/7/6.
 */
@SuppressWarnings("all")
public class SimpleHttpSessionHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(request instanceof ServletServerHttpRequest){
            ServletServerHttpRequest req = (ServletServerHttpRequest)request;
            HttpSession session = req.getServletRequest().getSession(false);
            if(session != null){
                User user = (User) session.getAttribute("user");
                if(user!=null)
                    attributes.put(Constant.WEBSOCKET_USER_NAME,user.getName());
                else
                    attributes.put(Constant.WEBSOCKET_USER_NAME,"test");
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
