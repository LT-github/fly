package com.lt.fly.web.req;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class HandicapSettlementReq {

	@NotNull(message = "返点类型不能为空")
	private Integer settlementType;
	@NotNull(message="起始时间不能为空")
	private Long settleStartTime;
	@NotNull(message="截止时间不能为空")
	private Long settleEndTime;
	private List<Long> handicapIds;
	//0 手动结算 1自动结算
	@NotNull(message="结算类型不能为空")
	private Integer type;
}
