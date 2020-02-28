package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * 赔率表
 */
@Getter
@Setter
@Entity
@Table(name = "t_odd")
public class Odd extends BasicEntity{

    //下注组
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name = "bet_group_id")
    private BetGroup betGroup;

    //某些玩法特殊的赔率
    @Column
    private String specificOdd;

    //赔率值
    @Column
    private Double oddValue;

    //赔率组
    @ManyToMany
    @JoinTable(name = "t_odd_group_odd",joinColumns = @JoinColumn(name = "odd_id"),
            inverseJoinColumns = @JoinColumn(name = " odd_group_id"))
    private Set<OddGroup> oddGroups;
}
