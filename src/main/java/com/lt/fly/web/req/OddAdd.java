package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OddAdd {

    @NotNull(message = "下注组不能为空")
    private Long betGroupId;

    @NotNull(message = "赔率值不能为空")
    private Double oddValue;
}
