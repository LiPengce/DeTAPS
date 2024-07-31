package com.lpc.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

/**
 * @author lpc
 * @version 1.0
 */
public class JwtUtil {
    private static final String KEY="lpc";

    public static String tokenGen(Map<String,Object>claims){
        String token = JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1)) //有效时间为1h
                .sign(Algorithm.HMAC256(KEY));

        return token;
    }

    public static Map<String,Object> tokenVerify(String token){
        try {
            System.out.println("JWT thread name： "+Thread.currentThread().getName());
            return JWT.require(Algorithm.HMAC256(KEY)).
                    build().verify(token).getClaim("claims").asMap();
        }catch (Exception e){
            throw e;
        }
    }

    public static void main(String[] args) {
        System.out.println("0xdbb0325695dd460c636f8db98ae9321cb72225a4e63be486cdad332a91bf0a77".length());
    }
}