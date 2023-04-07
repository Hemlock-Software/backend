package com.hemlock.www.backend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BackendApplication {
    public static RedisIO singleRedisIO;
    public static void main(String[] args) {
        singleRedisIO = new RedisIO();
        SpringApplication.run(BackendApplication.class, args);
    }

}
