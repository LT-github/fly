package com.lt.fly.web.resp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginResp {

    private Long id;

    private String nickname;

    private String username;

    private String token;

    public UserLoginResp(Long id, String nickname, String username, String token) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.token = token;
    }
}
