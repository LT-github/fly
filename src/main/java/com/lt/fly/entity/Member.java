package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "t_member")
@DiscriminatorValue("member")
public class Member extends User{
    // 该会员的财务信息
    @OneToMany(mappedBy = "createUser",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private Set<Finance> finances;

    // 该会员属于哪一个盘口
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name="handicap_id")
    private Handicap handicap;

    // 备注
    @Column(length = 255)
    private String remark;


}
