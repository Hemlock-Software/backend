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

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONResult<LoginReply> Login(@RequestBody LoginArgs args) {
        LoginReply reply = new LoginReply();
        reply.token = "your token here";

        Long storedKeyNum = BackendApplication.ColdData.Exists(args.mail);
        if(storedKeyNum == 0){
            return new JSONResult<LoginReply>("400", "No such mail!", reply);
        }

        String storedUserJson = BackendApplication.ColdData.Get(args.mail);

        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        if(Objects.equals(args.passwd, storedUserValue.Password)){
            return new JSONResult<LoginReply>("200", "success", reply);
        }else{
            return new JSONResult<LoginReply>("400", "Wrong password!", reply);
        }
    }

}

class LoginArgs{
    public String mail;
    public String passwd;
}
class LoginReply{
    public String token;
}