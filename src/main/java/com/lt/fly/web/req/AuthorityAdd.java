package com.lt.fly.web.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuthorityAdd {

	@NotNull(message = "权限名不能为空")
	private String name ;
	@NotNull(message = "权限描述不能为空")
	private String description;
	@NotNull(message = "权限路由标识不能为空")
	private String identifier;
	
	private Long parentId;
	
}
