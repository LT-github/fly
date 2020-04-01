package com.lt.fly.web.req;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class HandicapSettlementReq {

	@NotNull(message = "返点类型不能为空")
	private Integer settlementType;
	private Long settlementTime;
	private List<Long> handicapIds;
}
