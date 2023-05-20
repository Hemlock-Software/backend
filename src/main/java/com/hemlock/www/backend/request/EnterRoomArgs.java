package com.hemlock.www.backend.request;

import lombok.Data;
@Data
public class EnterRoomArgs {
    private String roomID;    //聊天室的ID
    private String password;//聊天室密码

//    public EnterRoomArgs(String name) {
//        this.name = name;
//    }
//
//    public EnterRoomArgs() {
//        this.name = "";
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
}
