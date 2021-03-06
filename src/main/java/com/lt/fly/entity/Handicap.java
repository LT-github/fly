package com.lt.fly.entity;



import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * 盘口
 */
@Entity
@Table(name = "t_handicap")
@Setter
@Getter
public class Handicap extends BasicEntity{
	
	// 盘口名称
	@Column(name = "name" , length = 32)
	private String name;
	
	// 鉴别器
	@Column(name="discriminator", insertable = false, updatable = false)
	private String discriminator;
	
	// 盘口的返点信息
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_handicap_proportion",joinColumns = @JoinColumn(name = "handicap_id"),
    inverseJoinColumns = @JoinColumn(name = " proportion_id"))
	private Set<Proportion> proportions;
	
	// 会员查看流水
	@ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="liushui_id")
	private DataDictionary liushui;
	
	// 会员查看盈亏
	@ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="yingkui_id")
	private DataDictionary yingkui;

	//会员
	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.EAGER , mappedBy = "handicap")
	private Set<Member> members;

	//赔率组
	@OneToOne
	@JoinColumn(name = "odd_group_id", referencedColumnName = "id")
	private OddGroup oddGroup;
	//<------------------------>
	
	//该盘口按时间结算(时分秒)
	@Column(name = "settlement_time")
	private String settlementTime;
	//该盘口按时间结算类型（0：手动结算 1：自动结算 2:禁用）
	@Column(name = "settlement_type")
	private Integer settlementType;
		
		
		
	

	
}
