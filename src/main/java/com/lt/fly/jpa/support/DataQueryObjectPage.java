package com.lt.fly.jpa.support;

/**
 * 分页查询对象
 * @author Fewstrong
 *
 */
public class DataQueryObjectPage extends DataQueryObjectSort {
	
	protected Integer page = 0;
	
	protected Integer size = 10;
	
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page - 1;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}


}
