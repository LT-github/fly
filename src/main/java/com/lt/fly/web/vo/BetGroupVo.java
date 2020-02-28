package com.lt.fly.web.vo;


import com.lt.fly.entity.BetGroup;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetGroupVo {
    private Long id;

    private String gameGroupName;

    private String betGrouopName;

    private Double odds;

    private Integer status;

    private Integer singleBetting;

    private Integer maximumBet;

    private String modifiedUsername;

    private Long modifiedTime;


    public BetGroupVo(){
        super();
    }

    public BetGroupVo(BetGroup obj){
        this.id = obj.getId();
        this.gameGroupName = obj.getGameGroup().getName();
        this.betGrouopName = obj.getName();
        this.status = obj.getStatus();
        this.singleBetting = obj.getSingleBetting();
        this.maximumBet = obj.getMaximumBet();
        this.modifiedUsername = obj.getModifyUser().getUsername();
        this.modifiedTime = obj.getModifyTime();
    }
}
