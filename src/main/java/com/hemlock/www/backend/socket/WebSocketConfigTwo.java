package com.hemlock.www.backend.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @program: backend
 * @description:
 * @create: 2023-05-20 14:42
 **/
@Configuration
@EnableWebSocket
public class WebSocketConfigTwo implements WebSocketConfigurer {


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocket(), "/websocket/*")//设置连接路径和处理
                .setAllowedOrigins("*");//设置拦截器
    }

    /**
     * 自定义拦截器拦截WebSocket请求
     */
//    class MyWebSocketInterceptor implements HandshakeInterceptor {
//
//        //前置拦截一般用来注册用户信息，绑定 WebSocketSession
//        @Override
//        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                                       WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//            System.out.println("前置拦截~~");
//
//            if (!(request instanceof ServletServerHttpRequest)) return true;
//
////            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
////            String userName = (String) servletRequest.getSession().getAttribute("userName");
//            String userName = "Koishipyb";
//            attributes.put("userName", userName);
//
//            return true;
//        }
//
//        @Override
//        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                                   WebSocketHandler wsHandler, Exception exception) {
//            System.out.println("后置拦截~~");
//        }
//    }
}