package com.lt.fly.web.vo;

import com.lt.fly.entity.Finance;
import com.lt.fly.entity.Member;
import com.lt.fly.utils.Arith;
import com.lt.fly.utils.FinanceUtil;
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
            List<Finance> finances = new ArrayList<>(obj.getFinances());
            this.rechargeTotal = Arith.add(FinanceUtil.getReduce(finances, RECHARGE),FinanceUtil.getReduce(finances, SYSTEM_RECHARGE));
            this.descendTotal = Arith.add(FinanceUtil.getReduce(finances, DESCEND),FinanceUtil.getReduce(finances, SYSTEM_DESCEND));
            this.waterTotal = Arith.sub(FinanceUtil.getReduce(finances, BET),FinanceUtil.getReduce(finances, BET_CANCLE));
            this.betResultTotal = Arith.sub(FinanceUtil.getReduce(finances, BET_RESULT),waterTotal);
            this.huiShuiTotal = Arith.sub(Arith.add(FinanceUtil.getReduce(finances, RANGE_LIUSHUI),FinanceUtil.getReduce(finances, TIMELY_LIUSHUI)),FinanceUtil.getReduce(finances,TIMELY_LISHUI_CANCLE));
            this.fenHongTotal = FinanceUtil.getReduce(finances, RANGE_YINGLI);
            this.balance = Arith.sub(Arith.add(fenHongTotal,huiShuiTotal,betResultTotal,rechargeTotal),descendTotal);
        }
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
