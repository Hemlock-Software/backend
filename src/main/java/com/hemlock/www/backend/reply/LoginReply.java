package com.hemlock.www.backend.reply;

import lombok.Data;

@Data
public class LoginReply {
    private String token;
    private String nickname;
    private Boolean isManager;
}
