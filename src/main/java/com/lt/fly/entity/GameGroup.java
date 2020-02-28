package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * 玩法组
 */
@Entity
@Table(name = "t_game_group")
@Getter
@Setter
public class GameGroup extends BasicEntity {

    // 玩法名称
    @Column( name = "name" , length = 32)
    private String name;

    @Column(name = "ping_yin_name" , length = 32)
    private String pingYinName;

    @Column
    // 玩法状态 0 正常 1禁用
    private Integer status;

    // 玩法描述
    @Column
    private String description;

    // 下注组
    @OneToMany(mappedBy = "gameGroup",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private Set<BetGroup> betGroups;

    @Column
    private String type;

    //玩法组类型
    @Column
    private Integer addType;


}
