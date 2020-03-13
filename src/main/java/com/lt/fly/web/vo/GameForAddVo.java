package com.lt.fly.web.vo;

import com.lt.fly.entity.BetGroup;
import com.lt.fly.entity.Odd;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameForAddVo {
    private Long id;

    private String gameGroupName;

    private String betGrouopName;

    private Integer status;

    private Integer singleBetting;

    private Integer maximumBet;

    private String modifiedUsername;

    private Long modifiedTime;

    private List<OddVo> oddVos = new ArrayList<>();

    public GameForAddVo() {
        super();
    }

    public GameForAddVo(BetGroup obj) {
        this.id = obj.getId();
        this.gameGroupName = obj.getGameGroup().getName();
        this.betGrouopName = obj.getName();
        this.status = obj.getStatus();
        this.singleBetting = obj.getSingleBetting();
        this.maximumBet = obj.getMaximumBet();
        this.modifiedUsername = obj.getModifyUser().getUsername();
        this.modifiedTime = obj.getModifyTime();
        if(null != obj.getOdds() && 0 != obj.getOdds().size()){
            this.oddVos = OddVo.tovo(new ArrayList<>(obj.getOdds()));
        }
    }

    public static List<GameForAddVo> tovo(List<BetGroup> betGroups){
        List<GameForAddVo> list = new ArrayList<>();
        for (BetGroup item :
                betGroups) {
            list.add(new GameForAddVo(item));
        }
        return list;
    }
}
