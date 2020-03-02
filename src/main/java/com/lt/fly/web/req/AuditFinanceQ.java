package com.lt.fly.web.req;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AuditFinanceQ extends DataQueryObjectPage{

	@QueryField(name="status",type=QueryType.EQUAL)
	private Integer status;	
	// 该条财务记录的金额
	@QueryField(name="money",type=QueryType.EQUAL)
	private Double money;	
	// 这笔财务的描述
	@QueryField(name="description",type=QueryType.EQUAL)
	private String description;	
	// 财务的审核状态  0 审核中 1审核通过 2审核失败
	@QueryField(name="auditStatus",type=QueryType.EQUAL)
	private Integer auditStatus;
	// 审核的类型  0 需要审核 1不需要审核	
	@QueryField(name="auditType",type=QueryType.EQUAL)
	private Integer auditType;
	//财务类型(1:充值,2:返点,3:投注)
	@QueryField(name="type",type=QueryType.EQUAL)
	private Integer type;
	
	
}
