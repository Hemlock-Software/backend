package com.hemlock.www.backend.room;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
public class RoomValueCold {
    //    private String ID;                   //Room序号
    private String name;                 //Room房间名
    private Member owner;                 //Room房主
    private String password;              //Room密码
    private int maxUsers;                 ///Room最大人数
<<<<<<< HEAD
    private ArrayList<Member> members = new ArrayList<>(); //Room成员
=======

    private Set<Member> members = new HashSet<>(); //Room成员
>>>>>>> caa978b9aaa20fb0653bd86f6b37a282bd8f61a7

    public void addMember(Member member) {
        this.members.add(member);
    }

    public int getRoomSize() {
        return this.members.size();
    }
}

