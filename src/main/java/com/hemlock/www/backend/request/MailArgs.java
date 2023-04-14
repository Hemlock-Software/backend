package com.hemlock.www.backend.request;

import lombok.Data;

@Data
public class MailArgs {
    private String mail;
    private int type;//1是注册，2是找回密码

    public String getMail() {
        return mail;
    }

    public int getType() {
        return type;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
