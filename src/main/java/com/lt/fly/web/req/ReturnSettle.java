package com.lt.fly.web.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReturnSettle {

    @NotNull(message = "返点类型不能为空")
    private Integer type;
}
