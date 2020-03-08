package com.lt.fly.web.vo;

import com.lt.fly.entity.Finance;
import com.lt.fly.entity.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetReportVo {
    //日期
    private String dateTime;
    //飞单数
    private Integer betCount;
    //飞单流水
    private Double water;
    //飞单盈亏
    private Double betResult;
    //上分数
    private Double rechargeCount;
    //下分数
    private Double descendCount;
    //回水
    private Double huiShui;
    //分红
    private Double fengHong;

    public BetReportVo() {
        super();
    }

//    public BetReportVo(Order order,Finance finance) {
////        this.dateTime = dateTime;
////        this.issueCount = issueCount;
////        this.betCount = betCount;
////        this.rechargeCount = rechargeCount;
////        this.descendCount = descendCount;
////        this.huiShui = huiShui;
////        this.fengHong = fengHong;
////    }


}
