package com.lt.fly.web.vo;

import java.util.ArrayList;
import java.util.List;
import com.lt.fly.entity.BankCard;

import lombok.Data;

@Data
public class BankCardVo {

private Integer status;
	
	//卡号	
	private String card;
	//银行卡所属人	
	private String realname;
	//银行卡预留手机号	
	private String phoneNumber;
	//开户行	
	private String bank;	
	//描述	
	private String description;
	//0银行卡 1其他	
	private Integer type;
	private String address;
    private Long id;	
	private Long createTime;	
	private Long modifyTime;
	private Long createUserId;
	private Long modifyUserId;
	private String modifyUserName;
	public BankCardVo() {}
	public BankCardVo(BankCard bankCard) {
		super();
		this.status = bankCard.getStatus();
		this.card = bankCard.getCard();
		this.realname = bankCard.getRealname();
		this.phoneNumber = bankCard.getPhoneNumber();
		this.bank = bankCard.getBank();
		this.description = bankCard.getDescription();
		this.type = bankCard.getType();
		this.address = bankCard.getAddress();
		this.id = bankCard.getId();
		this.createTime = bankCard.getCreateTime();
		this.modifyTime = bankCard.getModifyTime();
		if(bankCard.getCreateUser()!=null)
		this.createUserId = bankCard.getCreateUser().getId();
		if(bankCard.getModifyUser()!=null)
		this.modifyUserId = bankCard.getModifyUser().getId();
		if(bankCard.getModifyUser()!=null)
		this.modifyUserName=bankCard.getModifyUser().getUsername();
	}
	
	public static List<BankCardVo> toVo(List<BankCard> list){
		List<BankCardVo> resp = new ArrayList<BankCardVo>();
		for(BankCard item : list) {
			resp.add(new BankCardVo(item));
		}
		return resp;
	}
}
