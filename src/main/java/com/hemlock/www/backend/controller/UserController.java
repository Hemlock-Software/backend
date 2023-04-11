package com.hemlock.www.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.hemlock.www.backend.common.*;
import com.hemlock.www.backend.BackendApplication;
import com.hemlock.www.backend.reply.*;
import com.hemlock.www.backend.request.*;
import com.hemlock.www.backend.user.*;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;
import java.security.SecureRandom;


import java.util.Objects;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Random;


@RestController
@RequestMapping("/user")
public class UserController {


    public JSONResult<User> Hello() {
        User guest = new User("3052791719@qq.com", "ly", "123", true);
        JSONResult<User> res = new JSONResult<>("200", "success", guest);
        return res;
    }


    @RequestMapping(value = "/join", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")

    public JSONResult<String> Join(@RequestBody JoinArgs args) throws EmailException {
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

        if (args.getMail() == null || args.getPassword() == null) {
            return new JSONResult<LoginReply>("400", "Missing field", reply);
        }

        Long storedKeyNum = BackendApplication.ColdData.Exists(args.getMail());
        if (storedKeyNum == 0) {
            return new JSONResult<LoginReply>("400", "No such mail!", reply);
        }

        String storedUserJson = BackendApplication.ColdData.Get(args.getMail());
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);
        System.out.print(storedUserValue.getPassword());

        if (Objects.equals(args.getPassword(), storedUserValue.getPassword())) {
            reply.setToken(BackendApplication.TokenServer.SetToken(args.getMail()));
            return new JSONResult<LoginReply>("200", "success", reply);
        } else {
            return new JSONResult<LoginReply>("400", "Wrong password!", reply);
        }
    }

    @RequestMapping(value = "/checkToken", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONResult<String> Check(@RequestHeader("Authorization") String token) {
        //token会带前缀bearer ，从第七个字符开始
        if (BackendApplication.TokenServer.Verify(token.substring(7))) {
            return new JSONResult<String>("200", "ok",null);
        } else {
            return new JSONResult<String>("400", "wrong token",null);
        }

    }

    @RequestMapping(value = "/sendMail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONResult<String> sendVerificationCode (@RequestBody MailArgs args) throws EmailException {
        Long storedKeyNum = BackendApplication.ColdData.Exists(args.getMail());
        if (storedKeyNum>0){
            return new JSONResult<String>("400", "This mail has been used!", "");
        }
        StringBuffer uid = new StringBuffer();
        Random rd = new SecureRandom();
        for (int i=0;i<6;i++){
            uid.append(rd.nextInt(10));
        }
        VerificationCode verificationCode = new VerificationCode(uid.toString());
        String storeVerificationCode = JSON.toJSONString(verificationCode);

        HtmlEmail email=new HtmlEmail();//创建一个HtmlEmail实例对象
        email.setHostName("smtp.163.com");
        email.setCharset("utf-8");
        email.addTo(args.getMail());
        email.setFrom("zjuhemlock@163.com","Hemlock");
        email.setAuthentication("zjuhemlock@163.com","MKFEKBLWRITFOPNE");
        email.setSubject("铁树注册");//设置发送主题
        email.setMsg("欢迎注册Hemlock聊天室，您的验证码："+uid.toString());//设置发送内容
//        email.send();//进行发送
        if (BackendApplication.ColdData.Set("Verification"+args.getMail(), storeVerificationCode) ){
            email.send();
            return new JSONResult<String>("200", "success", "");

        }

        else
            return new JSONResult<String>("400", "fail", "");





    }
}


