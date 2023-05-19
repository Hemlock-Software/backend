package com.hemlock.www.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.hemlock.www.backend.Token.TokenData;
import com.hemlock.www.backend.common.*;
import com.hemlock.www.backend.BackendApplication;
import com.hemlock.www.backend.reply.LoginReply;
import com.hemlock.www.backend.request.FindPasswordArgs;
import com.hemlock.www.backend.request.JoinArgs;
import com.hemlock.www.backend.request.LoginArgs;
import com.hemlock.www.backend.request.MailArgs;
import com.hemlock.www.backend.user.*;
import io.netty.handler.codec.MessageAggregationException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;


import java.util.Objects;


import java.util.Random;


@CrossOrigin(methods = {RequestMethod.POST})

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/get-token", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> GetToken() {
        TokenData tokenData = new TokenData("this is test token data", null, TokenData.Type.Login);
        return ResponseEntity.status(HttpStatus.OK).body(BackendApplication.TokenServer.SetToken(tokenData));

    }

    /**
     * 用户注册
     *
     * @param args  请求体参数
     * @param token 用于注册认证的token，获取验证码时返回
     */
    @RequestMapping(value = "/join", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> Join(@RequestBody JoinArgs args, @RequestHeader("Authorization") String token) throws EmailException {
        if (args.getMail() == null || args.getIsManager() == null || args.getPassword() == null) {
//            return new JSONResult<String>("400", "Missing field", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing field!");
        }

        Long storedKeyNum = BackendApplication.ColdData.Exists(args.getMail());
        if (storedKeyNum > 0) {
//            return new JSONResult<String>("400", "This mail has been used!", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This mail has been used!");
        }

        UserValue userValue = new UserValue(args.getMail(), args.getPassword(), args.getIsManager());
        if (args.getNickname() != null) {
            userValue.setNickname(args.getNickname());
        }
        if (token.length() < 7) {
//            return new JSONResult<String>("400", "未携带验证码!", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No verification code is carried!");
        }
        String realToken = BackendApplication.TokenServer.Verify(token.substring(7));
        if (realToken == null) {
//            return new JSONResult<String>("400", "验证码过期", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification code expired!");

        }
        TokenData tokenData = JSON.parseObject(realToken, TokenData.class);
//        System.out.println(tokenData.getVerifyCode());
//        System.out.println(tokenData.getEmail());
//        System.out.println(args.getMail());
//        System.out.println(args.getVerifyCode());
        if (!Objects.equals(args.getVerifyCode(), tokenData.getVerifyCode()) || !Objects.equals(args.getMail(), tokenData.getEmail())) {

//            return new JSONResult<String>("400", "验证码错误", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification code error!");

        }
        String storedUserValue = JSON.toJSONString(userValue);

        if (BackendApplication.ColdData.Set(args.getMail(), storedUserValue))
//            return new JSONResult<String>("200", "success", "");
            return ResponseEntity.status(HttpStatus.OK).body("success!");
        else
//            return new JSONResult<String>("400", "fail", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DB error!");

    }

    /**
     * 用户登录
     *
     * @param args 登录请求体参数
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> Login(@RequestBody LoginArgs args) {
        LoginReply reply = new LoginReply();

        if (args.getMail() == null || args.getPassword() == null) {
//            return new JSONResult<LoginReply>("400", "Missing field", reply);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing field!");
        }

        Long storedKeyNum = BackendApplication.ColdData.Exists(args.getMail());
        if (storedKeyNum == 0) {
//            return new JSONResult<LoginReply>("400", "No such mail!", reply);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mail or Password Error!");
        }

        String storedUserJson = BackendApplication.ColdData.Get(args.getMail());
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);
//        System.out.print(storedUserValue.getPassword());

        if (Objects.equals(args.getPassword(), storedUserValue.getPassword())) {
            TokenData tokenData = new TokenData(args.getMail(), null, TokenData.Type.Login);
            reply.setToken(BackendApplication.TokenServer.SetToken(tokenData));
            return ResponseEntity.status(HttpStatus.OK).body(BackendApplication.TokenServer.SetToken(tokenData));
//            return new JSONResult<LoginReply>("200", "success", reply);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mail or Password Error!");
        }
    }

    /**
     * 发送用户注册验证的邮件
     *
     * @param args 发送右键的请求体
     * @throws EmailException 发送邮件的异常
     */
    @RequestMapping(value = "/send-mail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> sendVerificationCode(@RequestBody MailArgs args) throws EmailException {
//        Long storedKeyNum = BackendApplication.ColdData.Exists(args.getMail());
//        if (storedKeyNum > 0) {
//            return new JSONResult<String>("400", "This mail has been used!", "");
//        }

        StringBuffer uid = new StringBuffer();
        Random rd = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            uid.append(rd.nextInt(10));
        }
        VerificationCode verificationCode = new VerificationCode(uid.toString());
        String storeVerificationCode = JSON.toJSONString(verificationCode);

        int type = args.getType();

        try {
            for (int i = 0; i < 1; i++) {
                HtmlEmail email = new HtmlEmail();//创建一个HtmlEmail实例对象
                email.setHostName("smtp.qq.com");
                email.setCharset("utf-8");
                email.addTo(args.getMail());
                email.setFrom("2530250978@qq.com", "Hemlock");
                email.setAuthentication("2530250978@qq.com", "ppgsvwubatmodjad");
                if (type == 1) {
                    email.setSubject("铁树注册");//设置发送主题
                    email.setMsg("欢迎注册Hemlock聊天室，您的验证码：" + uid.toString());//设置发送内容
                } else if (type == 2) {
                    email.setSubject("铁树聊天室");//设置发送主题
                    email.setMsg("您正在找回Hemlock聊天室的密码，您的验证码：" + uid.toString());//设置发送内容
                }


                email.send();
            }
        } catch (EmailException e) {
            e.printStackTrace();
        } catch (MessageAggregationException e) {
            e.printStackTrace();
        }


        if (BackendApplication.ColdData.Set("Verification" + args.getMail(), storeVerificationCode)) {
            //email.send();
//            System.out.println(uid.toString());
            //将email和验证码放入token data，并转化为字符串，生成带有这两个变量的token
            TokenData tokenData = new TokenData(args.getMail(), uid.toString(), TokenData.Type.Register);
            String token = BackendApplication.TokenServer.SetToken(tokenData);   //10分钟过期

//            return new JSONResult<String>("200", "success", token);
            return ResponseEntity.status(HttpStatus.OK).body(token);

        } else
//            return new JSONResult<String>("400", "fail", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DB error!");


    }

    /**
     * 帮助用户找回密码
     *
     * @param args 找回密码的参数
     */
    @RequestMapping(value = "/find-password", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> findPassword(@RequestBody FindPasswordArgs args, @RequestHeader("Authorization") String token) {
        Long storedKeyNum = BackendApplication.ColdData.Exists(args.getMail());
        if (storedKeyNum == 0) {
//            return new JSONResult<String>("400", "This mail has never been used!", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This mail has never been used!");
        }

        if (args.getMail() == null || args.getPassword() == null) {
//            return new JSONResult<String>("400", "Missing field", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Field!");
        }

        if (token.length() < 7) {
//            return new JSONResult<String>("400", "未携带验证码!", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No verification code is carried!");
        }

        String realToken = BackendApplication.TokenServer.Verify(token.substring(7));
        if (realToken == null) {
//            return new JSONResult<String>("400", "验证码过期", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification code expired");
        }
        TokenData tokenData = JSON.parseObject(realToken, TokenData.class);
        if (!Objects.equals(args.getVerifyCode(), tokenData.getVerifyCode()) || !Objects.equals(args.getMail(), tokenData.getEmail())) {
//            return new JSONResult<String>("400", "验证码错误", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification code error!");
        }

        String storedUserJson = BackendApplication.ColdData.Get(args.getMail());
        UserValue storedUserValue = JSON.parseObject(storedUserJson, UserValue.class);

        storedUserValue.setPassword(args.getPassword());
        String newUserValue = JSON.toJSONString(storedUserValue);

        if (BackendApplication.ColdData.Set(args.getMail(), newUserValue))
//            return new JSONResult<String>("200", "success", "");
            return ResponseEntity.status(HttpStatus.OK).body("success!");

        else
//            return new JSONResult<String>("400", "fail", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("DB error!");

    }
}




