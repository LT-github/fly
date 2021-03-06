package com.lt.fly.entity;

import com.lt.fly.utils.GlobalConstant;
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
    @OneToMany(mappedBy = "createUser",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private Set<Finance> finances;

    // 该会员属于哪一个盘口
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinColumn(name="handicap_id")
    private Handicap handicap;

    // 备注
    @Column(length = 255)
    private String remark;

    //会员类型 ,1:普通会员.2:推手会员
    @Column
    private Integer type = GlobalConstant.MemberType.GENERAL.getCode();

    //是否存在盘口中   0:不在, 1:在
    @Column
    private Integer isHaveHandicap = GlobalConstant.IsHaveHandicap.NOT.getCode();

    //邀请码
    @Column
    private String referralCode;

    // 盘口的返点信息
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_member_proportion",joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = " proportion_id"))
    private Set<Proportion> proportions;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "security_id")
    private Security security;
}
