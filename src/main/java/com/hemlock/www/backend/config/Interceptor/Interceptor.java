package com.hemlock.www.backend.config.Interceptor;

import com.hemlock.www.backend.Token.TokenManager;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Interceptor implements WebMvcConfigurer {
    @Resource
    AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/user/join")      //拦截路径
                .addPathPatterns("/user/test-token")//拦截路径
                .addPathPatterns("/room/create-room")
                .addPathPatterns("/room/enter-room")
                .addPathPatterns("/room/getList")
                .addPathPatterns("/room/getRoomInfo")
                .addPathPatterns("/room/sendMessageTest")
                .addPathPatterns("/room/getMessageTest")
                .excludePathPatterns("/user/get-token")
                .excludePathPatterns("/user/login"); //放行路径
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*");
    }

}

