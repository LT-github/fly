package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "t_finance")
@Getter
@Setter
public class Finance extends BasicEntity{
	// 该条财务记录的金额
	@Column
	private Double money;
	
	// 这笔财务的描述
	@Column(length = 255)
	private String description;
	
	//用户当时余额
	@Column
	private Double balance;

	// 财务的审核状态  0 审核中 1审核通过 2审核失败
	@Column(name = "audit_status")
	private Integer auditStatus;

	//计数类型(1加 2减)
	@Column(name = "count_type")
	private Integer countType;

	//财务类型(1:充值,2:投注,3:撤销,4:实时返点,5:区间流水返点,6:区间盈利返点,7:下分)
	@Column
	private Integer type;

	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="order_id")
	private Order order;
}