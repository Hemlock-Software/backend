package com.hemlock.www.backend.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class User {
    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        String temp = "hello";
        String res = JSON.toJSONString(temp);
        return "welcome!";
    }

}
