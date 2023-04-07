package com.hemlock.www.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hemlock.www.backend.common.*;
import com.hemlock.www.backend.BackendApplication;
import com.hemlock.www.backend.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.sql.PreparedStatement;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public JSONResult<User> Hello() {
        User guest = new User("3052791719@qq.com", "ly", "123456", true);
        JSONResult<User> res = new JSONResult<>("200", "success", guest);
        return res;
    }

    @ResponseBody
    @GetMapping("/login")
    public String login(@RequestParam("mail") String mail, @RequestParam("passwd") String passwd) throws IOException {
        Long storedKeyNum = BackendApplication.singleRedisIO.Exists(mail);
        if(storedKeyNum == 0){
            return "no such mail";
        }

        String storedUserJson = BackendApplication.singleRedisIO.Get(mail);

        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        if(Objects.equals(passwd, storedUserValue.Password)){
            return "token:123456";
        }else{
            System.out.println(passwd + storedUserValue.Password);
            return passwd + storedUserValue.Password;
        }
    }

}
