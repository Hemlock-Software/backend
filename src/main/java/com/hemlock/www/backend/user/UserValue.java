package com.hemlock.www.backend.user;

import com.hemlock.www.backend.room.Member;
import com.hemlock.www.backend.room.RoomValueCold;
import lombok.Data;

import java.util.*;

@Data
public class UserValue {
//    private String mail;
    private String nickname;
    private String password;
    private Boolean isManager;

    private Set<UserStoredRoomValue> roomList = new HashSet<>();
    public void addRoom(String ID, String name){
        UserStoredRoomValue storedRoomValue  = new UserStoredRoomValue();
        storedRoomValue.setID(ID);
        storedRoomValue.setName(name);

        roomList.add(storedRoomValue);
    }
}
