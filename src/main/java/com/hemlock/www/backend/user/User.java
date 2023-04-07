package com.hemlock.www.backend.user;

import java.nio.channels.Pipe;

public class User {
    private String mail;
    private String nickname;
    private String password;
    private Boolean isManager;

    public User(String mail, String nickname, String password, Boolean isManager){
        this.mail = mail;
        this.nickname = nickname;
        this.password = password;
        this.isManager = isManager;
    }

    public String getMail() {
        return this.mail;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getPassword() {
        return this.password;
    }

    public Boolean getIsManager() {
        return this.isManager;
    }

}

