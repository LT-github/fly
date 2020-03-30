package com.lt.fly.web.vo;

import com.lt.fly.entity.GameGroup;
import com.lt.fly.utils.MyBeanUtils;
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
    private String description;



    public GameGroupVo() {
        super();
    }

    public GameGroupVo(GameGroup obj) {
        super();
        MyBeanUtils.copyProperties(obj,this);
    }

    public static List<GameGroupVo> tovo(List<GameGroup> gameGroups) {
        List<GameGroupVo> gameGroupVos = new ArrayList<GameGroupVo>();
        for (GameGroup item : gameGroups) {
            gameGroupVos.add(new GameGroupVo(item));
        }
        return gameGroupVos;
    }

}
