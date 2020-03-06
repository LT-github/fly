package com.lt.fly.web.query;


import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFind extends DataQueryObjectPage {

	
	@QueryField(name = "status" , type = QueryType.EQUAL)
	private Integer status;

}
