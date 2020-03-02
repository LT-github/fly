package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderAdd {
    private Long betGroupId;//下注组id

    private String betsContent;//下注内容

    private String description;//财务描述
}
