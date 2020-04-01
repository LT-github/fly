package com.lt.fly.web.req;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;

public class RoleQueryReq extends DataQueryObjectPage{

	@QueryField(name = "name" , type = QueryType.EQUAL)
	private String name;
	@QueryField(name = "id" , type = QueryType.EQUAL)	
	private Long id;
	@QueryField(name = "createTime" , type = QueryType.EQUAL)
	private Long createTime;
	@QueryField(name = "modifyTime" , type = QueryType.EQUAL)
	private Long modifyTime;
	@QueryField(name = "createUser.id" , type = QueryType.EQUAL)
	private Long createUserId;
	@QueryField(name = "modifyUser.id" , type = QueryType.EQUAL)
	private Long modifyUserId;
}
