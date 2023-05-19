package com.hemlock.www.backend.room;

import lombok.Data;

@Data
public class MessageKey {
    private String roomID;                   //Room序号
    private int messageID;               //Message序号
}
