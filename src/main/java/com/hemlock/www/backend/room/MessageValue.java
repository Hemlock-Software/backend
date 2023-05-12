package com.hemlock.www.backend.room;

import lombok.Data;

import java.sql.Time;
import java.util.ArrayList;

@Data
public class MessageValue {
    private String content;  //消息内容
    private Time time;       //发送时间
    private Member sender;     //消息发送者
}
