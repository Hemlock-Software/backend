package com.hemlock.www.backend.room;

import com.hemlock.www.backend.user.User;
import lombok.Data;

import java.util.ArrayList;

@Data
public class MessageKey {
    private String roomID;                   //Room序号
    private String messageID;               //Message序号
}
