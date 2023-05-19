package com.hemlock.www.backend.room;

import lombok.Data;

import java.util.ArrayList;

@Data
public class MessageValue {
    private String content;  //消息内容
    private String time;       //发送时间
    private Member sender;     //消息发送者
}
