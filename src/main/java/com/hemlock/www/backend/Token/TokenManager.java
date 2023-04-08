package com.hemlock.www.backend.Token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;


public class TokenManager {
    //token 过期时间，单位毫秒
    private static final long TTL=3600000;
    private static final String Key="b0252dc49ad20cb14ab968bd27c020504d9d657499e8c959996095b5e75fb1e2";

    private static String createJWT(String email){
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //sha256加密，密钥key
        JwtBuilder builder = Jwts.builder().setId(email)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, Key)
                .setExpiration(new Date( nowMillis + TTL));

        return builder.compact();
    }

    private static Claims decodeJWT(String jwt){
        return Jwts.parser()
                .setSigningKey(Key)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public String SetToken(String email){
        String result = null;
        try{
            result = TokenManager.createJWT(email);
        } catch (Exception e){
            System.out.println(e);
        }

        return result;
    }

    public Boolean Verify(String token){
        try{
            Claims result = TokenManager.decodeJWT(token);
            if(result!=null){
                //获取邮箱和过期时间
                System.out.println(result.getId());
                System.out.println(result.get("exp"));
            }
            return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }

    }

}
