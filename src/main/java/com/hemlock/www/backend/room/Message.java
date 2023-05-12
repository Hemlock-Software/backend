package com.hemlock.www.backend.room;

import lombok.Data;

import java.sql.Time;

import com.hemlock.www.backend.user.*;

@Data
public class Message {
    private String roomID;                   //Room序号
    private String messageID;               //Message序号

    private String content;  //消息内容
    private Time time;       //发送时间
    private User sender;     //消息发送者

//    public Message getMessage(String roomID,String messageID){
//
//    }
}
