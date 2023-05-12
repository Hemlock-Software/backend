package com.hemlock.www.backend.room;

import lombok.Data;

@Data
public class MessageKey {
    private String roomID;                   //Room序号
    private String messageID;               //Message序号
}
