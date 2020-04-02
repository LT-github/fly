package com.lt.fly.utils;

import com.lt.fly.entity.Finance;

import java.util.List;
import java.util.Set;


public class FinanceUtil {

    public static Double getReduce(List<Finance> finances, GlobalConstant.FinanceType financeType) {
        if (financeType.equals(financeType.RECHARGE) || financeType.equals(financeType.DESCEND)){
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
