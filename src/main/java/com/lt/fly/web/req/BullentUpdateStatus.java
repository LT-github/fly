package com.lt.fly.web.req;

import javax.validation.constraints.NotNull;

public class BullentUpdateStatus {

	
	//公告状态
	@NotNull
	private Integer status;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
