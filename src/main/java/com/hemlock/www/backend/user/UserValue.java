package com.hemlock.www.backend.user;

public class UserValue {
    public String Nickname;
    public String Password;
    public Boolean IsManager;

    public UserValue(String Nickname,String Password,Boolean IsManager){
        this.Nickname = Nickname;
        this.Password = Password;
        this.IsManager = IsManager;
    }
}
