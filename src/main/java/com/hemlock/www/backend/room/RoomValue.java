package com.hemlock.www.backend.room;

import com.hemlock.www.backend.user.User;
import lombok.Data;

import java.util.ArrayList;

@Data
public class RoomValue {
    private String name;                 //Room房间名
    private Member owner;                  //Room房主
    private ArrayList<Member> members = new ArrayList<>(); //Room成员

    public void addMember(Member member) {
        this.members.add(member);
    }
}
