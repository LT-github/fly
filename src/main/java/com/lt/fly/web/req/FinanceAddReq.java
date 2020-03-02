package com.lt.fly.web.req;

import javax.validation.constraints.NotNull;

import com.lt.fly.entity.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinanceAddReq {

	
	
	// 这笔财务的描述		
	private String description;			
	// 财务的审核状态  0 审核中 1审核通过 2审核失败	
	private Integer auditStatus;
	// 审核的类型  0 需要审核 1不需要审核		
	private Integer auditType;
	//财务类型(1:充值,2:返点,3:投注)	
	@NotNull(message = "财务类型不能为空！")
	private Integer type;
	//注单
	@NotNull(message = "注单不能为空！")
	private Order betsOrder;
    //该笔财务属于哪个会员id
	@NotNull(message = "会员id不能为空！")
	private Long memberId;
	
}
