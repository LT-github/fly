package com.lt.fly.web.vo;

import com.lt.fly.entity.Odd;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OddVo {

    private Long oddId;

    private Double oddValue;

    public OddVo(){
        super();
    }

    public OddVo(Odd obj){
        this.oddId = obj.getId();
        this.oddValue = obj.getOddValue();
    }

    public static List<OddVo> tovo(List<Odd> odds){
        List<OddVo> oddVos = new ArrayList<>();
        for (Odd item :
                odds) {
            oddVos.add(new OddVo(item));
        }
     return oddVos;
    }
}
