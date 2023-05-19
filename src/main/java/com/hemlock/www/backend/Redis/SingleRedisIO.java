package com.hemlock.www.backend.Redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class SingleRedisIO {
    private RedisClient myClient = null;
    private StatefulRedisConnection<String, String> myConnection = null;

    public SingleRedisIO(String ip, int port) {
        RedisURI redisUri = RedisURI.builder()
                .withHost(ip)
                .withPort(port)
                .withPassword("hemlock".toCharArray())
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();

        myClient = RedisClient.create(redisUri);
        myConnection = myClient.connect();
    }

    public void Exit() {
        myConnection.close();
        myClient.shutdown();
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

    public Long Increment(String key){
        RedisCommands<String, String> syncCommands = myConnection.sync();
        return syncCommands.incr(key);
    }

    public Boolean Expire(String key,Duration time){
        RedisCommands<String, String> syncCommands = myConnection.sync();
        return syncCommands.expire(key,time);
    }
}

