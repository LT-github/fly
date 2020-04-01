package com.lt.fly.web.req;

import java.util.List;

import lombok.Data;

@Data
public class HandicapSettlementReq {

	
	private Integer settlementType;
	private Long settlementTime;
	private List<Long> handicapIds;
}
