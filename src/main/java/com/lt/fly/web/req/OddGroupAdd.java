package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class OddGroupAdd {

    @NotNull(message = "名称不能为空")
    private String name;

    @NotNull(message = "赔率不能为空")
    private List<Long> oddIds;

}
