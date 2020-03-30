package com.lt.fly.web.vo;

import com.lt.fly.entity.Finance;
import com.lt.fly.entity.Member;
import com.lt.fly.utils.Arith;
import com.lt.fly.utils.GlobalConstant;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.lt.fly.utils.GlobalConstant.FinanceType.*;

@Data
public class MemberFinanceVo extends MemberVo {

    //余额
    private Double balance = 0.0;
    //总流水
    private Double waterTotal = 0.0;
    //总盈亏
    private Double betResultTotal = 0.0;
    //总上分
    private Double rechargeTotal = 0.0;
    //总下分
    private Double descendTotal = 0.0;
    //总回水
    private Double huiShuiTotal = 0.0;
    //总分红
    private Double fenHongTotal = 0.0;

    public MemberFinanceVo(Member obj) {
        super(obj);
        if (null != obj.getFinances() && 0 != obj.getFinances().size()) {
            Set<Finance> finances = obj.getFinances();
            this.rechargeTotal = Arith.add( getReduce(finances, RECHARGE),getReduce(finances, SYSTEM_RECHARGE));
            this.descendTotal = Arith.add(getReduce(finances, DESCEND),getReduce(finances, SYSTEM_DESCEND));
            this.waterTotal = Arith.sub(getReduce(finances, BET),getReduce(finances, BET_CANCLE));
            this.betResultTotal = Arith.sub(getReduce(finances, BET_RESULT),waterTotal);
            this.huiShuiTotal = Arith.sub(Arith.add(getReduce(finances, RANGE_LIUSHUI),getReduce(finances, TIMELY_LIUSHUI)),getReduce(finances,TIMELY_LISHUI_CANCLE));
            this.fenHongTotal = getReduce(finances, RANGE_YINGLI);
            this.balance = Arith.sub(Arith.add(fenHongTotal,huiShuiTotal,betResultTotal,rechargeTotal),descendTotal);
        }
    }

    private Double getReduce(Set<Finance> finances, GlobalConstant.FinanceType financeType) {
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

    public static List<MemberFinanceVo> tovo(List<Member> members){
        List<MemberFinanceVo> list = new ArrayList<>();
        for (Member item :
                members) {
            list.add(new MemberFinanceVo(item));
        }
        return list;
    }
}
