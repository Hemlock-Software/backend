package com.hemlock.www.backend.user;

public class UserValue {
    private String nickname;
    private String password;
    private Boolean isManager;

    public UserValue(String nickname,String password,Boolean isManager){
        this.nickname = nickname;
        this.password = password;
        this.isManager = isManager;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getIsManager() {
        return isManager;
    }

    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }
}
