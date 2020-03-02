package com.lt.fly.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lt.fly.Service.IBullentService;
import com.lt.fly.annotation.RequiredPermission;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.entity.Bullent;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.web.req.BullentUpdateStatus;
import com.lt.fly.web.req.BulletinAddReq;
import com.lt.fly.web.req.BulletinDeleteReq;
import com.lt.fly.web.req.FindBulletinsPageQo;
import com.lt.fly.web.req.FindUserBulletinsPageQo;
import com.lt.fly.web.req.UpdateBullentReq;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.BullentVo;



@RestController
@RequestMapping("/bullent")
public class BullentController extends BaseController{

	
	@Autowired
	IBullentService bullentService;
	
	//添加公告
	//@RequiredPermission(value="addBulletin")
	@PostMapping("add")
	@UserLoginToken
	public HttpResult<Object> add(@RequestBody BulletinAddReq req) throws ClientErrorException{
		
		bullentService.add(req);
		
		
		return HttpResult.success("添加成功！");
	}
	
	//删除公告
	//@RequiredPermission(value="deleteBulletin")
	@DeleteMapping("/d")
	@UserLoginToken
	public Object deleteBulletin(@RequestBody BulletinDeleteReq req) throws ClientErrorException{
		
		bullentService.deleteBulletin(req);
		return HttpResult.success("删除成功！");
	}
	
	//查询公告
	//@RequiredPermission(value="findBulletin")
	@PostMapping("/f")
	@UserLoginToken
	public Object findBulletin(@RequestBody FindBulletinsPageQo req) throws ClientErrorException{
		
		PageResp<BullentVo, Bullent> f = bullentService.findBulletin(req);
		
		return HttpResult.success(f,"查询成功！");
	}
	
	@PostMapping("/ub")
	@UserLoginToken
	public Object findUserBulletin(@RequestBody FindUserBulletinsPageQo req) throws ClientErrorException{
		
	 Object f = bullentService.findUserBulletin(req);
		
		return HttpResult.success(f,"查询成功！");
	}
	
	//修改公告状态
	//@RequiredPermission(value="updateBulletinStatus")
	@PostMapping("/up_s/{id}")
	@UserLoginToken
	public Object updateStatus(@RequestBody BullentUpdateStatus req,@PathVariable Long id) throws ClientErrorException{
		
		bullentService.updateStatus(req,id);
		return HttpResult.success("修改成功！");
		
	}
	
	//修改公告
	//@RequiredPermission(value="updateBullent")
	@PostMapping("/up_b")
	@UserLoginToken
	public Object updateBullent(@RequestBody UpdateBullentReq req) throws ClientErrorException{
		
		bullentService.updateBullent(req);
		return HttpResult.success("修改成功！");
		
	}
}
