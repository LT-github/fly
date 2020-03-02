package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindLiushuiReq {

	private Long before;
	private Long after;
	private Long memberId;
}
