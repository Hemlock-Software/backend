package com.hemlock.www.backend.user;

import java.nio.channels.Pipe;

public class User {
    public String Mail;
    public String Nickname;
    public String Password;
    public Boolean IsManager;

    public User(String Mail, String Nickname, String Password, Boolean IsManager){
        this.Mail = Mail;
        this.Nickname = Nickname;
        this.Password = Password;
        this.IsManager = IsManager;
    }

}

