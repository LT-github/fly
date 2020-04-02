package com.lt.fly.web.req;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;

import lombok.Getter;
import lombok.Setter;

/**
 * 银行卡请求类
 * @author Administrator
 *
 */
@Setter
@Getter
public class BankCardAddReq extends DataQueryObjectPage{

	
	
	    //0使用 1未使用 2删除
	    @QueryField(type = QueryType.EQUAL , name="status")
		private Integer status;		
		//卡号		
		private String card;		
		//属于哪个银行		
		private String bank;		
		//描述		
		private String description;
		//对应id
		private Long id;
		
		private Integer currentPage;
		
		private Integer type;
				
		private String address;
		
		
		
		
		
		

}
