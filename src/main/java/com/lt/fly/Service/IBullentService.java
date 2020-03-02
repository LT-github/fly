package com.lt.fly.Service;

import com.lt.fly.entity.Bullent;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.web.req.BullentUpdateStatus;
import com.lt.fly.web.req.BulletinAddReq;
import com.lt.fly.web.req.BulletinDeleteReq;
import com.lt.fly.web.req.FindBulletinsPageQo;
import com.lt.fly.web.req.FindUserBulletinsPageQo;
import com.lt.fly.web.req.UpdateBullentReq;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.BullentVo;

public interface IBullentService {


	void add(BulletinAddReq req) throws ClientErrorException;//添加公告
	PageResp<BullentVo,Bullent> findBulletin(FindBulletinsPageQo req) throws ClientErrorException;//查询所有公告
	Object deleteBulletin(BulletinDeleteReq req) throws ClientErrorException;//删公告
	Object findUserBulletin(FindUserBulletinsPageQo req) throws ClientErrorException;//客户端查询公告
	void updateStatus(BullentUpdateStatus req,Long id) throws ClientErrorException;//改变公告状态
	void updateBullent(UpdateBullentReq req) throws ClientErrorException;//修改草稿公告
	
}
