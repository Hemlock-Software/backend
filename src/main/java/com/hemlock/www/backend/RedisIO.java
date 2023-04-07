package com.hemlock.www.backend;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class RedisIO {
    private RedisClient myClient;
    private StatefulRedisConnection<String, String> myConnection;

    public RedisIO(){
        RedisURI redisUri = RedisURI.builder()
                .withHost("10.214.241.121")
                .withPort(15000)
                .withPassword("hemlock".toCharArray())
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();

        myClient = RedisClient.create(redisUri);
        myConnection = myClient.connect();
    }

    public String Get(String key){
        RedisCommands<String, String> syncCommands = myConnection.sync();
        return syncCommands.get(key);
    }

    public Boolean Set(String key,String val){
        RedisCommands<String, String> syncCommands = myConnection.sync();
        syncCommands.set(key,val);
        return true;
    }

    public Long Exists(String key){
        RedisCommands<String, String> syncCommands = myConnection.sync();
        return syncCommands.exists(key);
    }
}
