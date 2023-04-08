package com.hemlock.www.backend.controller;

import com.alibaba.fastjson2.JSON;

import com.hemlock.www.backend.common.*;
import com.hemlock.www.backend.BackendApplication;
import com.hemlock.www.backend.user.*;

import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public JSONResult<User> Hello() {
        User guest = new User("3052791719@qq.com", "ly", "123", true);
        JSONResult<User> res = new JSONResult<>("200", "success", guest);
        return res;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONResult<LoginReply> Login(@RequestBody LoginArgs args) {
        LoginReply reply = new LoginReply();


        Long storedKeyNum = BackendApplication.ColdData.Exists(args.mail);
        if(storedKeyNum == 0){
            return new JSONResult<LoginReply>("400", "No such mail!", reply);
        }

        String storedUserJson = BackendApplication.ColdData.Get(args.mail);

        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        if(Objects.equals(args.passwd, storedUserValue.Password)){
            reply.token = BackendApplication.TokenServer.SetToken(args.mail);
            return new JSONResult<LoginReply>("200", "success", reply);
        }else{
            return new JSONResult<LoginReply>("400", "Wrong password!", reply);
        }
    }

    @RequestMapping(value = "/checkToken", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONResult Check(@RequestHeader("Authorization") String token){
        //token会带前缀bearer ，从第七个字符开始
        if(BackendApplication.TokenServer.Verify(token.substring(7))){
            return new JSONResult("200","ok");
        }else{
            return new JSONResult("400","wrong token");
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