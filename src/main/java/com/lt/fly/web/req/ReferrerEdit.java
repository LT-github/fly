package com.lt.fly.web.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ReferrerEdit {

    @NotNull(message = "请选择至少一个返点规则")
    private List<Long> proportionIds;

}
