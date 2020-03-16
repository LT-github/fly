package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 下注组
 */
@Entity
@Table(name = "t_bet_group")
@Getter
@Setter
public class BetGroup extends BasicEntity{
    // 玩法内容的名称
    @Column(length = 32)
    private String name;

    // 玩法状态 0关闭 1开启
    @Column
    private Integer status;

    // 用户单次投注金额
    @Column
    private Integer singleBetting;

    // 单个玩法内容接收的最大投注金额（所有用户）
    @Column
    private Integer maximumBet;

    //关联的玩法组
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name="game_group_id")
    private GameGroup gameGroup;

    @OneToMany(mappedBy = "betGroup",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private Set<Odd> odds = new HashSet<>();

}
