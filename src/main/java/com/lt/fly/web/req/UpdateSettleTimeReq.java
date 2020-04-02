package com.lt.fly.web.req;

import lombok.Data;

@Data
public class UpdateSettleTimeReq {

	//统一结算的时间		
	private Long  settleTimeAll;
	//统一结算的状态（1选中 0未选中）
	private Integer  status;
}
