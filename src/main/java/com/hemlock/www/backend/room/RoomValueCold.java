package com.hemlock.www.backend.room;

import lombok.Data;

import java.util.ArrayList;

@Data
public class RoomValueCold {
    //    private String ID;                   //Room序号
    private String name;                 //Room房间名
    private Member owner;                 //Room房主
    private String password;              //Room密码
    private int maxUsers;                 ///Room最大人数
    private ArrayList<Member> members = new ArrayList<>(); //Room成员

    public void addMember(Member member) {
        this.members.add(member);
    }

    public int getRoomSize() {
        return this.members.size();
    }
}

