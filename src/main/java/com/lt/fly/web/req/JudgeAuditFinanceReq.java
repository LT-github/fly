package com.lt.fly.web.req;



import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JudgeAuditFinanceReq {

	// 财务的审核状态  0 审核中 1审核通过 2审核失败
	@NotNull(message = "审核状态不能为空")
	private Integer auditStatus;
	// 这笔财务的描述		
	private String description;
	//充值订单id
	private Long auditFinanceId;
	
}
