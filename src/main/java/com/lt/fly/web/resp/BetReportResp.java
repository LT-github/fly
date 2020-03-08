package com.lt.fly.web.resp;

import com.lt.fly.entity.Order;
import com.lt.fly.utils.MyBeanUtils;
import com.lt.fly.web.vo.BetReportVo;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter
public class BetReportResp extends PageResp{
    //总飞单数
    private Integer betCountTotal = 0;
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

    public BetReportResp(Page page) {
        this.setEleTotalNum(page.getTotalElements());
        this.setPageNum(page.getNumber());
        this.setPageSize(page.getSize());
        this.setTotalPage(page.getTotalPages());
    }
}
