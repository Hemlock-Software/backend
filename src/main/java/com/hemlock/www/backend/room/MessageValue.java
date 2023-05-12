package com.hemlock.www.backend.room;

import com.hemlock.www.backend.user.User;
import lombok.Data;

import java.sql.Time;
import java.util.ArrayList;

@Data
public class MessageValue {
    private String content;  //消息内容
    private Time time;       //发送时间
    private User sender;     //消息发送者
}
