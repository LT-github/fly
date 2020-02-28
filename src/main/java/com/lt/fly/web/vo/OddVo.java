package com.lt.fly.web.vo;

import com.lt.fly.entity.Odd;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OddVo {

    private Long oddId;

    private Double oddValue;

    private String betGroupName;

    private Long betGroupId;


    public OddVo(){
        super();
    }

    public OddVo(Odd obj){
        this.oddId = obj.getId();
        this.oddValue = obj.getOddValue();
        this.betGroupId = obj.getBetGroup().getId();
        this.betGroupName = obj.getBetGroup().getName();
    }
}
