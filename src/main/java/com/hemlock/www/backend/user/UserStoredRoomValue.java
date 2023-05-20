package com.hemlock.www.backend.user;

import com.hemlock.www.backend.room.Member;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserStoredRoomValue {
    private String ID;                   //Room序号
    private String name;                 //Room房间名
}
