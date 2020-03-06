package com.lt.fly.web.resp;

import com.lt.fly.entity.Order;
import com.lt.fly.utils.MyBeanUtils;
import com.lt.fly.web.vo.BetReportVo;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter
public class BetReportResp {
    //总飞单数
    private Integer betCountTotal;
    //总飞单金额
    private Double betMoneyTotal;
    //总盈亏
    private Double betResultTotal;
    //总上分
    private Double rechargeTotal;
    //总下分
    private Double descendTotal;
    //总回水
    private Double huiShuiTotal;
    //总分红
    private Double fenHongTotal;

    private List<BetReportVo> betReportVos;

}
