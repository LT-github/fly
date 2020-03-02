package com.lt.fly.web.req;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.QueryBetween;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindBulletinsPageQo extends  DataQueryObjectPage{

	@QueryField(type = QueryType.EQUAL , name="status")
	private Integer status;
	
	@QueryField(type = QueryType.EQUAL , name="id")
	private Long id;

	@QueryField(type = QueryType.BEWTEEN , name="pushTime")
	private QueryBetween<Long> pushTime;
	
	@QueryField(type = QueryType.BEWTEEN , name="pushEndTime")
	private QueryBetween<Long> pushEndTime;
				

	@QueryField(type = QueryType.BEWTEEN , name="createTime")
	private QueryBetween<Long> createTime;
	
	
	
	
}
