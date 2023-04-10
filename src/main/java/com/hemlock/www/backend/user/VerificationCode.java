package com.hemlock.www.backend.user;

public class VerificationCode {
    private String code;
    public VerificationCode(String code){
        this.code=code;
    }
    public String getCode(){return code;}
    public void setCode(String code){
        this.code=code;
    }
}
