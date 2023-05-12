package com.hemlock.www.backend.config.Interceptor;

import com.alibaba.fastjson2.JSON;
import com.hemlock.www.backend.Token.TokenData;
import com.hemlock.www.backend.Token.TokenManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final TokenManager tokenManager = new TokenManager();
    private static final String HEADER_AUTH = "Authorization";
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse Response, Object object) throws Exception {
        Response.setCharacterEncoding("UTF-8");
        Response.setContentType("application/json; charset=utf-8");

        String token = httpServletRequest.getHeader(HEADER_AUTH);// 从 http 请求头中取出 token

        if(token==null||token.length()<7){
            PrintWriter printWriter = Response.getWriter();
            printWriter.write("{\"message\":\"Unauthorized: No token.\"}");
            printWriter.flush();
            printWriter.close();

            return false;
        }
        token=token.substring(7);
        String realToken = tokenManager.Verify(token);
        if(realToken==null){
            Response.setStatus(400);
            PrintWriter printWriter = Response.getWriter();
            printWriter.write("{\"message\":\"Unauthorized: Wrong token.\"}");
            printWriter.flush();
            printWriter.close();
            return false;
        }
        TokenData tokenData = JSON.parseObject(realToken, TokenData.class);
        httpServletRequest.setAttribute("email",tokenData.getEmail());
        return true;


    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}