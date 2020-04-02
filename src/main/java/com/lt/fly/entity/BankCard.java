package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_bank_card")
@Setter
@Getter
public class BankCard extends BasicEntity{

	//0使用 1未使用 2删除
	@Column(name = "status")
	private Integer status;
	
	//卡号
	@Column(name = "card")
	private String card;

	//银行卡所属人
	@Column
	private String realname;

	//银行卡预留手机号
	@Column
	private String phoneNumber;

	//开户行
	@Column(name = "bank")
	private String bank;
	
	//描述
	@Column(name = "description")
	private String description;

	//0银行卡 1其他
	@Column(name = "type")
	private Integer type;
	
	@Column(name = "address")
	private String address;

}
