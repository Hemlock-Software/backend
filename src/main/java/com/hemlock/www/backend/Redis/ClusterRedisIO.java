package com.hemlock.www.backend.Redis;

import com.hemlock.www.backend.MessageQueue.Subscriber;
import com.hemlock.www.backend.MessageQueue.Subscriber;
import com.hemlock.www.backend.room.Message;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.cluster.pubsub.RedisClusterPubSubAdapter;
import io.lettuce.core.cluster.pubsub.StatefulRedisClusterPubSubConnection;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.RedisPubSubListener;


import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ClusterRedisIO {
    private RedisClusterClient myClient = null;
    private StatefulRedisClusterConnection<String, String> myConnection = null;
    private StatefulRedisClusterPubSubConnection<String, String> pubConnection = null, subConnection=null;
    public ClusterRedisIO(String ip, int port) {
        RedisURI redisUri = RedisURI.builder()
                .withHost(ip)
                .withPort(port)
                .withPassword("hemlock".toCharArray())
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();

        myClient = RedisClusterClient.create(redisUri);
        myConnection = myClient.connect();

        pubConnection = myClient.connectPubSub();
        subConnection = myClient.connectPubSub();
        subConnection.addListener(new MyRedisClusterPubSubListener());
    }


    public void Exit() {
        myConnection.close();
        myClient.shutdown();
        subConnection.close();
        pubConnection.close();
    }

    public void Subscribe(String channel){
        subConnection.sync().subscribe(channel);
        System.out.println("subsribe "+channel);
    }
    public void SendMSG(String roomId,String message){
        RedisClusterCommands<String,String> commands=pubConnection.sync();
        commands.publish(roomId,message);
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
