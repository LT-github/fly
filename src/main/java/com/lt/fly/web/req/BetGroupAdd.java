package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetGroupAdd {

    //玩法组id
    private Long gameGroupId;

    //玩法内容名称
    private String name;

    //玩法状态
    private Integer status;

    //玩法赔率
    private Double odds;

    //单次下注金额
    private Integer singleBetting;

    //最大下注金额
    private Integer maximumBet;

}
