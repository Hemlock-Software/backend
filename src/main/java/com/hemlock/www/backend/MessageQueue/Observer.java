package com.hemlock.www.backend.MessageQueue;

public abstract class Observer {
    //subscribe储存的是room id
    protected String subscribe;
    public abstract void update(String roomId,String message);
    public String getSubscribe(){
        return subscribe;
    }
}
