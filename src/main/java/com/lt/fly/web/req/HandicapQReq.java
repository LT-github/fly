package com.lt.fly.web.req;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;

public class HandicapQReq extends DataQueryObjectPage{

	@QueryField(name = "settlementType" , type = QueryType.EQUAL)
	private Integer settlementType;
}
