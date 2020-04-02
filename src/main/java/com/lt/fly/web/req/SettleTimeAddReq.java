package com.lt.fly.web.req;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SettleTimeAddReq {

	//统一结算的时间
	@NotNull(message = "添加结算时间不能为空")
	private Long  settleTimeAll;
	//统一结算的状态（1选中 0未选中）
	//private Integer  status;
}
