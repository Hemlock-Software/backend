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


    public JSONResult<User> Hello() {
        User guest = new User("3052791719@qq.com", "ly", "123", true);
        JSONResult<User> res = new JSONResult<>("200", "success", guest);
        return res;
    }


    @RequestMapping(value = "/join", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONResult<String> Join(@RequestBody JoinArgs args) {
        if (args.getMail() == null || args.getIsManager() == null || args.getPassword() == null) {
            return new JSONResult<String>("400", "Missing field", "");
        }

        Long storedKeyNum = BackendApplication.ColdData.Exists(args.getMail());
        if (storedKeyNum > 0) {
            return new JSONResult<String>("400", "This mail has been used!", "");
        }

        UserValue userValue = new UserValue(args.getMail(), args.getPassword(), args.getIsManager());
        if (args.getNickname() != null) {
            userValue.setNickname(args.getNickname());
        }

        String storedUserValue = JSON.toJSONString(userValue);

        if (BackendApplication.ColdData.Set(args.getMail(), storedUserValue))
            return new JSONResult<String>("200", "success", "");

        else
            return new JSONResult<String>("400", "fail", "");

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONResult<LoginReply> Login(@RequestBody LoginArgs args) {
        LoginReply reply = new LoginReply();

        Long storedKeyNum = BackendApplication.ColdData.Exists(args.getMail());
        if (storedKeyNum == 0) {
            return new JSONResult<LoginReply>("400", "No such mail!", reply);
        }

        String storedUserJson = BackendApplication.ColdData.Get(args.getMail());

        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        if (Objects.equals(args.getPassword(), storedUserValue.getPassword())) {
            reply.setToken(BackendApplication.TokenServer.SetToken(args.getMail()));
            return new JSONResult<LoginReply>("200", "success", reply);
        } else {
            return new JSONResult<LoginReply>("400", "Wrong password!", reply);
        }
    }

    @RequestMapping(value = "/checkToken", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONResult Check(@RequestHeader("Authorization") String token) {
        //token会带前缀bearer ，从第七个字符开始
        if (BackendApplication.TokenServer.Verify(token.substring(7))) {
            return new JSONResult("200", "ok");
        } else {
            return new JSONResult("400", "wrong token");
        }

    }

}

class LoginArgs {
    private String mail;
    private String password;

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}

class LoginReply {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


class JoinArgs {
    private String mail;

    private String password;

    private String nickname;

    private Boolean isManager;

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public Boolean getIsManager() {
        return isManager;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
