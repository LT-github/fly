package com.lt.fly.web.vo;

import com.lt.fly.entity.Finance;
import com.lt.fly.entity.Order;
import com.lt.fly.utils.Arith;
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
    private Integer betCount;
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
        this.recharge = Arith.add( getReduce(finances, RECHARGE),getReduce(finances, SYSTEM_RECHARGE));
        this.descend = Arith.add(getReduce(finances, DESCEND),getReduce(finances, SYSTEM_DESCEND));
        this.winMoney = getReduce(finances,BET_RESULT);
        this.water = Arith.sub(getReduce(finances, BET),getReduce(finances, BET_CANCLE));
        this.betResult = Arith.sub(winMoney,water);
        this.huiShui = Arith.sub(Arith.add(getReduce(finances, RANGE_LIUSHUI),getReduce(finances, TIMELY_LIUSHUI)),getReduce(finances,TIMELY_LISHUI_CANCLE));
        this.fengHong = getReduce(finances, RANGE_YINGLI);
    }


    private Double getReduce(List<Finance> finances, GlobalConstant.FinanceType financeType) {
        if (financeType.equals(RECHARGE) || financeType.equals(DESCEND)){
            return finances.stream().filter(finance -> finance.getType().equals(financeType.getCode())
                    && finance.getAuditStatus().equals(GlobalConstant.AuditStatus.AUDIT_PASS.getCode()))
                    .map(Finance::getMoney)
                    .reduce(0.0, (a, b) -> Arith.add(a, b));
        }
        return finances.stream().filter(finance -> finance.getType().equals(financeType.getCode()))
                .map(Finance::getMoney)
                .reduce(0.0, (a, b) -> Arith.add(a, b));
    }
}
