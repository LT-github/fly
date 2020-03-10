package com.lt.fly.web.vo;

import com.lt.fly.entity.Handicap;
import com.lt.fly.entity.Member;
import com.lt.fly.entity.Odd;
import com.lt.fly.entity.Proportion;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class HandicapVo {
    private Long id;
    private String name;
    private List<ProportionVo> proportions;
    private String oddGroupName;
    private List<MemberVo> memberVos;
    private DataDictionaryVo liushui;
    private DataDictionaryVo yinkui;

    public HandicapVo() {
        super();
    }

    public HandicapVo(Handicap obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        // 设置返点
        this.proportions = ProportionVo.toVo(new ArrayList<>(obj.getProportions()));
        // 设置流水、盈亏的查询
        this.liushui = new DataDictionaryVo(obj.getLiushui());
        this.yinkui = new DataDictionaryVo(obj.getYingkui());
        this.oddGroupName = obj.getOddGroup().getName();
        //设置门店用户
        if(null != obj.getMembers()){
            this.memberVos = MemberVo.toVo(new ArrayList<>(obj.getMembers()));
        }
    }

    public static List<HandicapVo> toVo(List<Handicap> list){
        List<HandicapVo> handicapVos = new ArrayList<>();
        for(Handicap item:list) {
            handicapVos.add(new HandicapVo(item));
        }
        return handicapVos;
    }
}
