package com.hemlock.www.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.hemlock.www.backend.Token.TokenData;
import com.hemlock.www.backend.common.*;
import com.hemlock.www.backend.BackendApplication;
import com.hemlock.www.backend.reply.LoginReply;
import com.hemlock.www.backend.request.JoinArgs;
import com.hemlock.www.backend.request.LoginArgs;
import com.hemlock.www.backend.request.MailArgs;
import com.hemlock.www.backend.user.*;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;


import java.util.Objects;


import java.util.Random;



@RestController
@RequestMapping("/user")
public class UserController {


    @RequestMapping(value = "/hello", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONResult<User> Hello() {
        User guest = new User("3052791719@qq.com", "ly", "123", true);
        JSONResult<User> res = new JSONResult<>("200", "success", guest);
        return res;
    }


    /**
     * 用户注册
     * @param args 请求体参数
     * @param token 用于注册认证的token，获取验证码时返回
     *
     */
    @RequestMapping(value = "/join", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONResult<String> Join(@RequestBody JoinArgs args, @RequestHeader("Authorization") String token) throws EmailException {
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
        if(token.length()<7){
            return new JSONResult<String>("400", "未携带验证码!", "");
        }
        String realToken = BackendApplication.TokenServer.Verify(token.substring(7));
        if(realToken==null){
            return new JSONResult<String>("400","验证码过期","");
        }
        TokenData tokenData = JSON.parseObject(realToken, TokenData.class);
        System.out.println(tokenData.getVerifyCode());
        System.out.println(tokenData.getEmail());
        System.out.println(args.getMail());
        System.out.println(args.getVerifyCode());
        if (!Objects.equals(args.getVerifyCode(),tokenData.getVerifyCode())||!Objects.equals(args.getMail(),tokenData.getEmail())){
            return new JSONResult<String>("400", "验证码错误", "");
        }
        String storedUserValue = JSON.toJSONString(userValue);

        if (BackendApplication.ColdData.Set(args.getMail(), storedUserValue))
            return new JSONResult<String>("200", "success", "");

        else
            return new JSONResult<String>("400", "fail", "");

    }

    /**
     * 用户登录
     * @param args 登录请求体参数
     *
     */
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
            TokenData tokenData = new TokenData(args.getMail(), null, TokenData.Type.Login);
            reply.setToken(BackendApplication.TokenServer.SetToken(tokenData));
            return new JSONResult<LoginReply>("200", "success", reply);
        } else {
            return new JSONResult<LoginReply>("400", "Wrong password!", reply);
        }
    }

    /**
     * 校对token
     * @param token 用于校对用户登录的token
     */
    @RequestMapping(value = "/checkToken", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JSONResult<String> Check(@RequestHeader("Authorization") String token) {
        //token会带前缀bearer ，从第七个字符开始
        if(token.length()<7){
            return new JSONResult<String>("400","no token",null);
        }
        String TokenData=BackendApplication.TokenServer.Verify(token.substring(7));
        if (TokenData!=null) {
            return new JSONResult<String>("200", "ok",TokenData);
        } else {
            return new JSONResult<String>("400", "wrong token",null);
        }

    }

    /**
     * 发送用户注册验证的邮件
     * @param args 发送右键的请求体
     * @throws EmailException 发送邮件的异常
     */
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


        for (int i=0;i<1;i++){
            HtmlEmail email=new HtmlEmail();//创建一个HtmlEmail实例对象
            email.setHostName("smtp.qq.com");
            email.setCharset("utf-8");
            email.addTo(args.getMail());
            email.setFrom("2530250978@qq.com","Hemlock");
            email.setAuthentication("2530250978@qq.com","ppgsvwubatmodjad");
            email.setSubject("铁树注册");//设置发送主题
            email.setMsg("欢迎注册Hemlock聊天室，您的验证码："+uid.toString());//设置发送内容
            email.send();
        }



        if (BackendApplication.ColdData.Set("Verification"+args.getMail(), storeVerificationCode) ){
            //email.send();
            System.out.println(uid.toString());
            //将email和验证码放入token data，并转化为字符串，生成带有这两个变量的token
            TokenData tokenData = new TokenData(args.getMail(), uid.toString(), TokenData.Type.Register);
            String token=BackendApplication.TokenServer.SetToken(tokenData);   //10分钟过期
            return new JSONResult<String>("200", "success", token);

        }

        else
            return new JSONResult<String>("400", "fail", "");


    }
}


