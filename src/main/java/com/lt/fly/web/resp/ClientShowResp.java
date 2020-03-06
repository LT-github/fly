package com.lt.fly.web.resp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientShowResp {
    //今日飞单总额
    private Double betTotal;
    //今日飞单总盈亏
    private Double betResult;
    //今日飞单期数
    private Integer issueCount;
    //今日回水
    private Double returnTotal;
    //今日分红
    private Double dividendTotal;
}
