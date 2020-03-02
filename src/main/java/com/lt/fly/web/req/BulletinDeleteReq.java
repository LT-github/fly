package com.lt.fly.web.req;

import java.util.List;

public class BulletinDeleteReq {

	//公告id
	private List<Long> ids;

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	
}
