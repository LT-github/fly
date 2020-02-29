package com.lt.fly.web.vo;

import com.lt.fly.entity.OddGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class OddGroupVo {

    private Long oddGroupId;

    private String name;

    private List<OddVo> oddVos;

    public OddGroupVo(){
        super();
    }

    public OddGroupVo(OddGroup obj){
        this.oddGroupId = obj.getId();
        this.name = obj.getName();
        this.oddVos = OddVo.tovo(new ArrayList<>(obj.getOdds()));
    }

    public static List<OddGroupVo> tovo(List<OddGroup> oddGroups){
        List<OddGroupVo> oddGroupVos = new ArrayList<>();
        for (OddGroup item :
                oddGroups) {
            oddGroupVos.add(new OddGroupVo(item));
        }
        return oddGroupVos;
    }
}
