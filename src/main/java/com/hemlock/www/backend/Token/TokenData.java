package com.hemlock.www.backend.Token;

public class TokenData {
    public String Email;
    public String VerifyCode;
    //Token 作用：登录/注册
    public enum Type{
        Register,Login;
    }
    public Type Usage;
    public TokenData(String email,String verifycode,Type type){
        this.Email=email;
        this.VerifyCode=verifycode;
        this.Usage=type;
    }
}
