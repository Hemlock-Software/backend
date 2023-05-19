package com.hemlock.www.backend.Token;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class TokenManager {
    //token 过期时间，单位毫秒
    private static final long TTL=3600000;
    private static final long VerifyTTL=600000;
    private static final String Key="b0252dc49ad20cb14ab968bd27c020504d9d657499e8c959996095b5e75fb1e2";

    private static String createJWT(String data, long ttl){
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        StringBuffer uid = new StringBuffer();
        Random rd = new SecureRandom();
        for (int i=0;i<6;i++){
            uid.append(rd.nextInt(10));
        }
        //sha256加密，密钥key
        JwtBuilder builder = Jwts.builder().setId(uid.toString())
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, Key)
                .claim("body", data)
                .setExpiration(new Date( nowMillis + ttl));

        return builder.compact();
    }

    private static Claims decodeJWT(String jwt){
        return Jwts.parser()
                .setSigningKey(Key)
                .parseClaimsJws(jwt)
                .getBody();
    }

    // 功能：生成一个token
    // 传入参数：@data：需要保存在token中的变量，类型为字符串，可以将一个类转化为字符串传入
    //          @ttl：token过期时间，单位是毫秒，可以传入0，会设置为默认一小时过期
    //  返回值：成功生成则返回token的string，失败返回null
    //
    public String SetToken(TokenData tokenData){
        String result = null;
        long ttl=TTL;
        if(tokenData.getUsage()== TokenData.Type.Register){
            ttl=VerifyTTL;
        }
        try{
            result = TokenManager.createJWT(JSON.toJSONString(tokenData),ttl);
        } catch (Exception e){
            System.out.println(e);
        }

        return result;
    }

    // 功能：验证token
    // 传入参数：@token：客户端发来的token
    //  返回值：token有效返回token中保存的字符串，无效返回null
    //
    public String Verify(String token){
        try{
            Claims result = TokenManager.decodeJWT(token);
            if(result!=null){
                //获取TokenData和过期时间
                //System.out.println(result.get("body"));
                //System.out.println(result.get("exp"));
            }
            return result.get("body").toString();
        } catch (Exception e){
            //System.out.println(e);
            return null;
        }

    }

    public String getTime(String token){
        try{
            Claims result = TokenManager.decodeJWT(token);
            if(result!=null){
                //获取TokenData和过期时间
                //System.out.println(result.get("body"));
                //System.out.println(result.get("exp"));
            }
            return result.get("exp").toString();
        } catch (Exception e){
            System.out.println(e);
            return null;
        }

    }

}
