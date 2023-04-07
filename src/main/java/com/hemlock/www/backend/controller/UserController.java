package com.hemlock.www.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hemlock.www.backend.user.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    @ResponseBody
    @RequestMapping("/hello")
    public String Hello() {
        User guest = new User("1@qq.com","ly","123",true);
        String res = JSON.toJSONString(guest);
        return res ;

    }

}
