package com.hemlock.www.backend.room;

import lombok.Data;
@Data
public class EnterRoomArgs {
    private String name;
    private String owner; //房主邮箱;后续可以改动

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
