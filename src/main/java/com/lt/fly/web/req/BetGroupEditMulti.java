package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BetGroupEditMulti {

    private List<Long> ids;

    private Integer singleBetting; //单次最低下注金额

    private Integer maximumBet; 		 //最大下注金额

    private Integer status; //玩法状态
}
