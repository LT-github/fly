package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class DataDictionaryAdd{
	
	private Long parentId;
	
	@NotNull(message = "字典名不能为空")
	private String name;

	private String value;
	
	private Integer status = 0;
	
	private Integer sortNo;
	
	
}
