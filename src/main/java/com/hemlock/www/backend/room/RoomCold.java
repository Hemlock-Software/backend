package com.hemlock.www.backend.room;

import lombok.Data;

import java.util.*;

@Data
public class RoomCold {
    private String ID;                   //Room序号
    private String name;                 //Room房间名
    private Member owner;                  //Room房主
    private ArrayList<Member> members = new ArrayList<>();     //Room成员
    public void addMember(Member member) {
        this.members.add(member);
    }
}

