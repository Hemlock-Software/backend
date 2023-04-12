package com.hemlock.www.backend.Token;

public class TokenData {
    private String email;
    private String verifyCode;
    //Token 作用：登录/注册
    public enum Type{
        Register,Login;
    }
    private Type usage;

    public void setEmail(String Email) {
        email = Email;
    }

    public void setUsage(Type Usage) {
        usage=Usage;
    }

    public void setVerifyCode(String VerifyCode) {
        verifyCode = VerifyCode;
    }

    public String getEmail() {
        return email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public Type getUsage() {
        return usage;
    }

    public TokenData(String email, String verifycode, Type type){
        this.email=email;
        this.verifyCode=verifycode;
        this.usage=type;
    }
}
