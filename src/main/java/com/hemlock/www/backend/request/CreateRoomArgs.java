package com.hemlock.www.backend.request;

import lombok.Data;

@Data
public class CreateRoomArgs {
    private String name;     //房间名称
    private int maxUsers;    //聊天室最大人数
    private String password; //聊天室密码

}
