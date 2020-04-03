package com.lt.fly.web.resp;

import com.lt.fly.utils.Arith;
import com.lt.fly.web.vo.BetReportVo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class ReportResp<T, L> extends PageResp<T, L>{
    //总飞单数
    private Long betCountTotal;
    //总飞单金额
    private Double waterTotal;
    //总战果
    private Double winMoneyTotal;
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

    public ReportResp(Integer pageNum, Integer pageSize, Integer totalPage, Long eleTotalNum, List<T> data,List<BetReportVo> vos) {
        super(pageNum, pageSize, totalPage, eleTotalNum, data);

        this.betCountTotal = vos.stream().map(BetReportVo::getBetCount).reduce(0l,(a,b) -> a + b);
        this.waterTotal = vos.stream().map(BetReportVo::getWater).reduce(0.0,(a,b) -> Arith.add(a,b));
        this.winMoneyTotal = vos.stream().map(BetReportVo::getWinMoney).reduce(0.0,(a,b) -> Arith.add(a,b));
        this.betResultTotal = vos.stream().map(BetReportVo::getBetResult).reduce(0.0,(a,b) -> Arith.add(a,b));
        this.rechargeTotal = vos.stream().map(BetReportVo::getRecharge).reduce(0.0,(a,b) -> Arith.add(a,b));
        this.descendTotal = vos.stream().map(BetReportVo::getDescend).reduce(0.0,(a,b) -> Arith.add(a,b));
        this.huiShuiTotal = vos.stream().map(BetReportVo::getHuiShui).reduce(0.0,(a,b) -> Arith.add(a,b));
        this.fenHongTotal = vos.stream().map(BetReportVo::getFengHong).reduce(0.0,(a,b) -> Arith.add(a,b));
    }

    public ReportResp() {
        super();
    }

    public ReportResp(Page page) {
        this.setEleTotalNum(page.getTotalElements());
        this.setPageNum(page.getNumber());
        this.setPageSize(page.getSize());
        this.setTotalPage(page.getTotalPages());
    }
}
