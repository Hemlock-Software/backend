package com.hemlock.www.backend.Redis;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ClusterRedisIO {
    private RedisClusterClient myClient = null;
    private StatefulRedisClusterConnection<String, String> myConnection = null;

    public ClusterRedisIO(String ip, int port) {
        RedisURI redisUri = RedisURI.builder()
                .withHost(ip)
                .withPort(port)
                .withPassword("hemlock".toCharArray())
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();

        myClient = RedisClusterClient.create(redisUri);
        myConnection = myClient.connect();
    }

    public void Exit() {
        myConnection.close();
        myClient.shutdown();
    }

    public String Get(String key){
        RedisClusterCommands<String, String> syncCommands = myConnection.sync();
        return syncCommands.get(key);
    }

    public Boolean Set(String key,String val){
        RedisClusterCommands<String, String> syncCommands = myConnection.sync();
        syncCommands.set(key,val);
        return true;
    }

    public Long Exists(String key){
        RedisClusterCommands<String, String> syncCommands = myConnection.sync();
        return syncCommands.exists(key);
    }

    public Long Increment(String key){
        RedisClusterCommands<String, String> syncCommands = myConnection.sync();
        return syncCommands.incr(key);
    }

    public Boolean Expire(String key,Duration time){
        RedisClusterCommands<String, String> syncCommands = myConnection.sync();
        return syncCommands.expire(key,time);
    }
}
