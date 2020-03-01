package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetGroupEdit {

    private Integer singleBetting; //单次最低下注金额

    private Integer maximumBet; 		 //最大下注金额

    private Integer status; //玩法状态

    private Long oddId;

    private Long oddGroupId;
}
