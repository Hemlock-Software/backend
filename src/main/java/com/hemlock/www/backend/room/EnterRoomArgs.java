package com.hemlock.www.backend.room;

import lombok.Data;
@Data
public class EnterRoomArgs {
    private String name;  //用户信息，后续要删除
    private String ID;    //聊天室的ID
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
