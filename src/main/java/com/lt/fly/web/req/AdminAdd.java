package com.lt.fly.web.req;

import javax.validation.constraints.NotNull;

public class AdminAdd extends AdminEdit{
    @NotNull(message = "用户名不能为空")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
