package com.hemlock.www.backend.Redis;

import com.hemlock.www.backend.MessageQueue.Subscriber;
import io.lettuce.core.cluster.pubsub.RedisClusterPubSubAdapter;
import io.lettuce.core.pubsub.RedisPubSubAdapter;

public class MyRedisClusterPubSubListener extends RedisPubSubAdapter<String, String> {

    @Override
    public void message(String channel, String message) {
        System.out.println("Message received from channel " + channel + ": " + message);
        Subscriber.getInstance().notifyAllObservers(channel,message);
    }
}