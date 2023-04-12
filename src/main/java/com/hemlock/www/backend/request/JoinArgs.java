package com.hemlock.www.backend.request;

public class JoinArgs {
    private String mail;

    private String password;

    private String nickname; //用户昵称

    private Boolean isManager;  //是否为管理员

    private String verifyCode; //验证码

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

    public String getVerifyCode() {
        return verifyCode;
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

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
