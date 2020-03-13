package com.lt.fly.web.req;

import lombok.Data;

import java.util.List;

import javax.validation.constraints.NotNull;

@Data
public class RoleAdd {
	
	@NotNull(message = "角色名称不能为空")
	private String name;
	
	@NotNull(message = "描述不能为空")
	private String description;
	
	private List<Long> authIds;

}
