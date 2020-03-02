package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "t_finance")
@Getter
@Setter
public class Finance extends BasicEntity{

	// 该条财务记录的状态  0 正常 、1 禁用
	@Column
	private Integer status;
	
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

	// 审核的类型  0 需要审核 1不需要审核
	@Column(name = "audit_type")
	private Integer auditType;

	//计数类型(1加 2减)
	@Column(name = "count_type")
	private Integer countType;

	//财务类型(1:充值,2:返点,3:投注)
	@Column
	private Integer type;
}