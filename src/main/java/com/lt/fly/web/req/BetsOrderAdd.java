package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BetsOrderAdd {
    private Long gameContentId;//下注组id

    private String betsContent;//下注内容

    private String description;//财务描述
}
