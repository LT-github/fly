package com.lt.fly.web.req;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindLiushuiReq {

	@NotNull(message = "请选择时间前区间")
	private Long before;
	@NotNull(message = "请选择时间后区间")
	private Long after;
	@NotNull(message = "会员id不能为空")
	private Long memberId;
}
