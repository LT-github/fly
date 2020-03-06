package com.lt.fly.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_bank_card")
public class BankCard extends BasicEntity{

	//0使用 1未使用 2删除
	@Column(name = "status")
	private Integer status;
	
	//卡号
	@Column(name = "card")
	private String card;
	
	//属于哪个银行
	@Column(name = "bank")
	private String bank;
	
	//描述
	@Column(name = "description")
	private String description;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}
