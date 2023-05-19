package com.hemlock.www.backend.room;

import lombok.Data;

@Data
public class Member {
    private String mail;
    private String nickname;

    public Member(String mail, String nickname) {
        this.mail = mail;
        this.nickname = nickname;
    }

    public Member() {
        this.mail = "";
        this.nickname = "";
    }
}
