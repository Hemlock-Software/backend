package com.hemlock.www.backend.room;

import com.hemlock.www.backend.user.User;
import lombok.Data;

@Data
public class CreateRoomArgs {
    private String user;  //房主的邮箱
    private String name;  //房间名称

}
