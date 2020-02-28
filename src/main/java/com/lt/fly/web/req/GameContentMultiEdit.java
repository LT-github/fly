package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameContentMultiEdit {
    private List<Long> ids = new ArrayList<>();

    private Double odds; //赔率

    private Integer singleBetting; //单次最低下注金额

    private Integer maximumBet; 		 //最大下注金额

    private Integer status;//玩法状态
}
