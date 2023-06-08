package com.hemlock.www.backend.MessageQueue;

import java.util.ArrayList;
import java.util.List;

public class Subscriber {
    private final List<Observer> observers = new ArrayList<Observer>();
    public void attach(Observer observer){
        observers.add(observer);
    }
    private static final Subscriber instance = new Subscriber();
    private Subscriber(){}
    public static Subscriber getInstance(){
        return instance;
    }
    public void remove(Observer observer){
        observers.remove(observer);
    }

    public void notifyAllObservers(String roomId,String message){
        for(Observer observer:observers){
               observer.update(roomId,message);
        }
    }
}
