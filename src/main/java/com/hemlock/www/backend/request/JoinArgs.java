package com.hemlock.www.backend.request;

public class JoinArgs {
    private String mail;

    private String password;

    private String nickname;

    private Boolean isManager;

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public Boolean getIsManager() {
        return isManager;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
