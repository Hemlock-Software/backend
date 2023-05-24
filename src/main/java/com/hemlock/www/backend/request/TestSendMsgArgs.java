package com.hemlock.www.backend.request;

import lombok.Data;

@Data
public class TestSendMsgArgs {
    private String id;      //RoomID
    private String content; //消息内容
}
