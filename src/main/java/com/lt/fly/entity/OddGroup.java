package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "t_odd_group")
@Getter
@Setter
public class OddGroup extends BasicEntity{

    //名字
    @Column
    private String name;

    @ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinTable(name = "t_odd_group_odd",joinColumns = @JoinColumn(name = "odd_group_id"),
            inverseJoinColumns = @JoinColumn(name = " odd_id"))
    private Set<Odd> odds;

    @OneToOne
    private Handicap handicap;

}
