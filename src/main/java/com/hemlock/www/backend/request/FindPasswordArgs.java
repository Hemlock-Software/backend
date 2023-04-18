package com.hemlock.www.backend.request;

import lombok.Data;

@Data
public class FindPasswordArgs {
    private String mail;

    private String password;   //新的密码

    private String verifyCode; //验证码

    public FindPasswordArgs(String mail, String password, String verifyCode) {
        this.mail = mail;
        this.password = password;
        this.verifyCode = verifyCode;
    }


}
