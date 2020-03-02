package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class HandicapAdd {

    @NotNull(message = "用户组名称不能为空")
    private String name;

    @NotNull(message = "请选择至少一个返点规则")
    private List<Long> proportionIds;

    @NotNull(message = "请选择打票流水")
    private Long liushuiId;

    @NotNull(message = "请选择盈亏")
    private Long yinkunId;

    private List<Long> memberIds;
}
