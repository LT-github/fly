package com.lt.fly.web.req;


import com.lt.fly.jpa.support.DataQueryObjectSort;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataDictionaryFind extends DataQueryObjectSort {

	@QueryField(name = "parent.id" ,type = QueryType.EQUAL)
	private Long parentId;


}
