package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProportionAdd {
	
	@NotNull(message = "请选择至少一个返点类型")
	private Long returnPointTypeId;
	
	@NotNull(message = "请填写比例")
	private Double proportionVal;
	
	@NotNull(message = "请填写比例描述")
	private String description;
	
	private String rangeMoney;
	
}
