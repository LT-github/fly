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

    public OddGroupVo(){
        super();
    }

    public OddGroupVo(OddGroup obj){
        this.oddGroupId = obj.getId();
        this.name = obj.getName();
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
