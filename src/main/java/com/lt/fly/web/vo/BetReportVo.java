package com.lt.fly.web.vo;

import com.lt.fly.entity.Finance;
import com.lt.fly.entity.Order;
import com.lt.fly.utils.Arith;
import com.lt.fly.utils.FinanceUtil;
import com.lt.fly.utils.GlobalConstant;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

import static com.lt.fly.utils.GlobalConstant.FinanceType.*;

@Getter
@Setter
public class BetReportVo {
    //日期
    private String dateTime;
    //飞单数
    private Long betCount;
    //飞单流水
    private Double water;
    //飞单战果
    private Double winMoney;
    //飞单盈亏
    private Double betResult;
    //上分数
    private Double recharge;
    //下分数
    private Double descend;
    //回水
    private Double huiShui;
    //分红
    private Double fengHong;

    public BetReportVo() {
    }

    public BetReportVo(String dateTime ,List<Finance> finances) {
        this.dateTime = dateTime;
        this.recharge = Arith.add(FinanceUtil.getReduce(finances, RECHARGE),FinanceUtil.getReduce(finances, SYSTEM_RECHARGE));
        this.descend = Arith.add(FinanceUtil.getReduce(finances, DESCEND),FinanceUtil.getReduce(finances, SYSTEM_DESCEND));
        this.winMoney = FinanceUtil.getReduce(finances,BET_RESULT);
        this.water = Arith.sub(FinanceUtil.getReduce(finances, BET),FinanceUtil.getReduce(finances, BET_CANCLE));
        this.betResult = Arith.sub(winMoney,water);
        this.huiShui = Arith.sub(Arith.add(FinanceUtil.getReduce(finances, RANGE_LIUSHUI),FinanceUtil.getReduce(finances, TIMELY_LIUSHUI)),FinanceUtil.getReduce(finances,TIMELY_LISHUI_CANCLE));
        this.fengHong = FinanceUtil.getReduce(finances, RANGE_YINGLI);
        this.betCount = finances.stream().filter(finance -> finance.getType().equals(BET.getCode())).count()-finances.stream().filter(finance -> finance.getType().equals(BET_CANCLE.getCode())).count();
    }


}
