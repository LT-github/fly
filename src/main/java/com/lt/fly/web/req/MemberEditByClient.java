package com.lt.fly.web.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MemberEditByClient extends MemberEdit{

    @NotNull(message = "状态不能为空")
    private Integer status;

    private Long handicapId;

    private String remark;

    @NotNull(message = "会员类型不能为空")
    private Integer type;

}
