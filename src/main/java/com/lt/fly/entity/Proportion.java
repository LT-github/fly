package com.lt.fly.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * 返点比例
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_proportion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator")
@DiscriminatorValue("Proportion")
@Data
public class Proportion extends BasicEntity{
	
	// 返点名称的数据字典
	@ManyToOne(cascade={CascadeType.MERGE})
	@JoinColumn(name="returnPoint_id") 
	private DataDictionary returnPoint;
	
	// 比例的值
	@Column(name = "proportion_val")
	private Double proportionVal;
	
	@Column(name = "description" , length = 255)
	private String description;
	// 金额范围，例：10000-20000
	@Column(name = "ranges")
	private String ranges;
	
	// 该返点对应的店铺组
	@ManyToMany(mappedBy = "proportions")
	private Set<Handicap> handicaps;
	
	
	// 鉴别器
	@Column(name="discriminator", insertable = false, updatable = false)
	private String discriminator;

	// 该返点对应的店铺组
	@ManyToMany(mappedBy = "proportions")
	private Set<Member> members;

}