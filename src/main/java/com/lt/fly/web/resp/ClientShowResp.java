package com.lt.fly.web.resp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientShowResp {
    //今日飞单总额
    private double betTotal;
    //今日飞单总盈亏
    private double betResult;
    //今日飞单期数
    private Integer issueCount;
    //区间回水
    private double rangeTotal;
    //实时回水
    private double timelyTotal;
    //今日分红
    private double dividendTotal;
}
