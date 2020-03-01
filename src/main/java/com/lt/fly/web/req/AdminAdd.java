package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AdminAdd extends AdminEdit{
    @NotNull(message = "用户名不能为空")
    private String username;
}
