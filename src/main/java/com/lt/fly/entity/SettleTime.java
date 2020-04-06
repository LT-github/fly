package com.lt.fly.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_settle_time")
@Getter
@Setter
public class SettleTime extends BasicEntity{

	//统一结算的时间
	@Column
	private String  settleTimeAll;
	//统一结算的状态（1选中 0未选中）
	@Column
	private Integer  status;
	
}
