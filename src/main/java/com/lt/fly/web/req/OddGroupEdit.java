package com.lt.fly.web.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OddGroupEdit {
    @NotNull(message = "赔率组名称不能为空")
    private String name;

}
