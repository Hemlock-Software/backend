package com.hemlock.www.backend.MessageQueue;

import java.io.IOException;

public abstract class Observer {
    //subscribe储存的是room id
    protected String subscribe;
    public abstract void update(String roomId,String message) throws IOException;
    public String getSubscribe(){
        return subscribe;
    }
}
