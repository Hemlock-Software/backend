package com.hemlock.www.backend;


import com.hemlock.www.backend.Redis.ClusterRedisIO;
import com.hemlock.www.backend.Redis.SingleRedisIO;
import com.hemlock.www.backend.Token.TokenManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BackendApplication {
    public static SingleRedisIO ColdData = null;
    public static ClusterRedisIO HotData = null;
    public static TokenManager TokenServer = null;
    public static void main(String[] args) {
        ColdData = new SingleRedisIO("10.214.241.121",15000);
        HotData = new ClusterRedisIO("10.214.241.121",15010);
        TokenServer = new TokenManager();
        SpringApplication.run(BackendApplication.class, args);
    }

}
