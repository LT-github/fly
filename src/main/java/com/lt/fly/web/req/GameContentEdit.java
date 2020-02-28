package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameContentEdit {

    //单次最低下注金额
    private Integer singleBetting;

    //最大下注金额
    private Integer maximumBet;

    //玩法状态
    private Integer status;
}
