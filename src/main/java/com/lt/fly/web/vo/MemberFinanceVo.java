package com.lt.fly.web.vo;

import com.lt.fly.entity.Finance;
import com.lt.fly.entity.Member;
import com.lt.fly.utils.Arith;
import com.lt.fly.utils.GlobalConstant;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.lt.fly.utils.GlobalConstant.FinanceType.*;

@Data
public class MemberFinanceVo extends MemberVo {

    //余额
    private Double balance = 0.0;
    //总飞单金额
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
            for (Finance item :
                    obj.getFinances()) {
                //上分
                if (item.getType().equals(RECHARGE.getCode())
                        && item.getAuditStatus().equals(GlobalConstant.AuditStatus.AUDIT_PASS.getCode())){
                    //上分+
                    rechargeTotal = Arith.add(rechargeTotal,item.getMoney());
                    //余额+
                    balance = Arith.add(balance,item.getMoney());
                }
                //下分
                if (item.getType().equals(DESCEND.getCode())) {
                    //下分+
                    descendTotal = Arith.add(descendTotal,item.getMoney());
                    //余额-
                    balance = Arith.sub(balance,item.getMoney());
                }
                //下注
                if (item.getType().equals(BET.getCode())){
                    //流水+
                    waterTotal = Arith.add(waterTotal,item.getMoney());
                    //余额-
                    balance = Arith.sub(balance,item.getMoney());
                    //盈利-
                    betResultTotal = Arith.sub(betResultTotal,item.getMoney());
                }
                //撤销
                if (item.getType().equals(CANCLE.getCode())){
                    //流水-
                    waterTotal = Arith.sub(waterTotal,item.getMoney());
                    //余额+
                    balance = Arith.add(balance,item.getMoney());
                    //盈利+
                    betResultTotal = Arith.add(betResultTotal,item.getMoney());
                }
                //下注获胜
                if (item.getType().equals(BET_WIN.getCode())) {
                    //余额+
                    balance = Arith.add(balance,item.getMoney());
                    //盈利+
                    betResultTotal = Arith.add(betResultTotal,item.getMoney());
                }
                //实时流水返水//区间流水返点
                if (item.getType().equals(TIMELY_LIUSHUI.getCode())
                        || item.getType().equals(RANGE_LIUSHUI.getCode())) {
                    //回水+
                    huiShuiTotal = Arith.add(huiShuiTotal,item.getMoney());
                    //余额+
                    balance = Arith.add(balance,item.getMoney());
                }
                //区间分红
                if (item.getType().equals(RANGE_YINGLI.getCode())) {
                    //分红+
                    fenHongTotal = Arith.add(fenHongTotal,item.getMoney());
                    //余额+
                    balance = Arith.add(balance,item.getMoney());
                }
                //系统下分
                if (item.getType().equals(SYSTEM_DESCEND)) {
                    //余额-
                    balance = Arith.sub(balance,item.getMoney());
                }
                //系统上分
                if (item.getType().equals(SYSTEM_RECHARGE)) {
                    //余额+
                    balance = Arith.add(balance,item.getMoney());
                }

            }
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
