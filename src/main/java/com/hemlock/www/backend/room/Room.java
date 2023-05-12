package com.hemlock.www.backend.room;

import lombok.Data;
import com.hemlock.www.backend.user.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.reflect.Array;
import java.util.*;

@Data
public class Room {
    private String ID;                   //Room序号
    private String name;                 //Room房间名
    private Member owner;                 //Room房主
    private int maxUsers;                 ///Room最大人数
    private ArrayList<Member> members = new ArrayList<>();     //Room成员
//    private ArrayList<Message> messages = new ArrayList<>(); //Room消息集合

    public void addMember(Member member) {
        this.members.add(member);
    }

//    public void addMessage(Message message) {
//        this.messages.add(message);
//    }
}

