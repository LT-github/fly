package com.lt.fly.web.controller;

import com.lt.fly.Service.IBullentService;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.entity.Bullent;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.web.log.Log;
import com.lt.fly.web.req.*;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.BullentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/bullent")
public class BullentController extends BaseController{

	
	@Autowired
	IBullentService bullentService;
	
	//添加公告
	//@RequiredPermission(value="addBulletin")
	@Log(value = "添加公告")
	@PostMapping("add")
	@UserLoginToken
	public HttpResult<Object> add(@RequestBody BulletinAddReq req) throws ClientErrorException{
		
		bullentService.add(req);
		
		
		return HttpResult.success("添加成功！");
	}
	
	//删除公告
	//@RequiredPermission(value="deleteBulletin")
	@Log(value = "删除公告")
	@DeleteMapping("/d")
	@UserLoginToken
	public Object deleteBulletin(@RequestBody BulletinDeleteReq req) throws ClientErrorException{
		
		bullentService.deleteBulletin(req);
		return HttpResult.success("删除成功！");
	}
	
	//查询公告
	//@RequiredPermission(value="findBulletin")
	@Log(value = "查询公告列表")
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
	@Log(value = "修改公告状态")
	@PostMapping("/up_s/{id}")
	@UserLoginToken
	public Object updateStatus(@RequestBody BullentUpdateStatus req,@PathVariable Long id) throws ClientErrorException{
		
		bullentService.updateStatus(req,id);
		return HttpResult.success("修改成功！");
		
	}
	
	//修改公告
	//@RequiredPermission(value="updateBullent")
	@Log(value = "修改公告信息")
	@PostMapping("/up_b")
	@UserLoginToken
	public Object updateBullent(@RequestBody UpdateBullentReq req) throws ClientErrorException{
		
		bullentService.updateBullent(req);
		return HttpResult.success("修改成功！");
		
	}
}
