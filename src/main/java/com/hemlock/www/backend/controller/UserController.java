package com.hemlock.www.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hemlock.www.backend.common.*;
import com.hemlock.www.backend.user.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.PreparedStatement;

@RestController
public class UserController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public JSONResult<User> Hello() {
        User guest = new User("3052791719@qq.com", "ly", "123456", true);
        JSONResult<User> res = new JSONResult<>("200", "success", guest);

        return res;

    }

}
