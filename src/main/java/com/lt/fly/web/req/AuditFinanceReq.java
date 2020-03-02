package com.lt.fly.web.req;


import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditFinanceReq {


	// 该条财务记录的状态  0 正常 、1 禁用	
	private Integer status;	
	// 该条财务记录的金额
	@NotNull(message = "金额不能为空！")
	private Double money;	
	// 这笔财务的描述
	private String description;	
	// 财务的审核状态  0 审核中 1审核通过 2审核失败	
	private Integer auditStatus;
	// 审核的类型  0 需要审核 1不需要审核	
	private Integer auditType;	
	//财务类型(1:充值,2:返点,3:投注)
	@NotNull(message = "财务类型不能为空！")
	private Integer type;
	
	
}
