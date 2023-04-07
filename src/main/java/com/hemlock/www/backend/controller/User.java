package com.hemlock.www.backend.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

@Controller
public class User {
    @ResponseBody
    @RequestMapping("/hello")
    public String hello() {
        Jedis jedis = new Jedis("10.214.241.121",15000);
        //jedis的所有命令就是我们之前学习的所有指令
        // 2.测试连接是否成功，连接成功之后输出PONG
        jedis.auth("hemlock");
        System.out.println(jedis.ping());

        String temp = "hello";
        String res = JSON.toJSONString(temp);
        return "welcome!";
    }

}
