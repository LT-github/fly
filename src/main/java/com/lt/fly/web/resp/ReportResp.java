package com.lt.fly.web.resp;

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

    public ReportResp(Integer pageNum, Integer pageSize, Integer totalPage, Long eleTotalNum, List<T> data) {
        super(pageNum, pageSize, totalPage, eleTotalNum, data);
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
