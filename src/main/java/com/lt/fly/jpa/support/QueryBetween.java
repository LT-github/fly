package com.lt.fly.jpa.support;

import lombok.Data;

/**
 * 
 * 用于比较查询
 * 
 * @author Fewstrong
 */

@Data
public class QueryBetween<T extends Comparable<?>>{

	public T before;
	public T after;
	
	

}
