package com.lt.fly.web.vo;

import com.lt.fly.entity.GameGroup;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameGroupVo {

    private Long id;
    private String name;
    private String type;
    private String pingYinName;
    private Integer status;



    public GameGroupVo() {
        super();
    }

    public GameGroupVo(GameGroup obj) {
        super();
        this.id = obj.getId();
        this.name = obj.getName();
        this.type = obj.getType();
        this.pingYinName = obj.getPingYinName();
        this.status = obj.getStatus();
    }

    public static List<GameGroupVo> tovo(List<GameGroup> gameGroups) {
        List<GameGroupVo> gameGroupVos = new ArrayList<GameGroupVo>();
        for (GameGroup item : gameGroups) {
            gameGroupVos.add(new GameGroupVo(item));
        }
        return gameGroupVos;
    }

}
