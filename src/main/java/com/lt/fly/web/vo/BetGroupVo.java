package com.lt.fly.web.vo;


import com.lt.fly.entity.BetGroup;
import com.lt.fly.entity.Odd;
import com.lt.fly.utils.GlobalConstant;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BetGroupVo {
    private Long id;

    private String gameGroupName;

    private String betGrouopName;

    private Integer status;

    private Integer singleBetting;

    private Double oddValue;

    private Integer maximumBet;

    private String modifiedUsername;

    private Long modifiedTime;


    public BetGroupVo(){
        super();
    }

    public BetGroupVo(BetGroup obj, Odd odd){
        this.id = obj.getId();
        this.gameGroupName = obj.getGameGroup().getName();
        this.betGrouopName = obj.getName();
        if(GlobalConstant.GameStatus.CLOSE.getCode() == obj.getGameGroup().getStatus())
            this.status = GlobalConstant.GameStatus.CLOSE.getCode();
        else
            this.status = obj.getStatus();
        this.singleBetting = obj.getSingleBetting();
        this.maximumBet = obj.getMaximumBet();
        if (null!=obj.getModifyUser())
            this.modifiedUsername = obj.getModifyUser().getUsername();
        this.modifiedTime = obj.getModifyTime();
        if (null != odd) {
            this.oddValue = odd.getOddValue();
        }
    }

    public static List<BetGroupVo> tovo(List<BetGroup> betGroups){
        List<BetGroupVo> betGroupVos = new ArrayList<>();
        for (BetGroup item :
                betGroups) {
            betGroupVos.add(new BetGroupVo(item,null));
        }
        return betGroupVos;
    }

}
