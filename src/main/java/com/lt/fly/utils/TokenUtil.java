package com.lt.fly.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.lt.fly.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    public String getToken(User u) {
        String token="";
        token= JWT.create().withAudience(u.getId()+"")// 将 user id 保存到 token 里面
                .sign(Algorithm.HMAC256(u.getPassword()));// 以 password 作为 token 的密钥
        return token;
    }

    public String getToken(User u, String clientIp) {
        String tokenValue = u.getId() + "-" + clientIp;
        String token= JWT.create().withAudience(tokenValue)// 将 user id 保存到 token 里面
                .sign(Algorithm.HMAC256(u.getPassword()));// 以 password 作为 token 的密钥
        return token;
    }
}
