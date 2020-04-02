package com.lt.fly.web.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lt.fly.dao.ISettleTimeRepository;
import com.lt.fly.dao.IUserRepository;
import com.lt.fly.entity.SettleTime;
import com.lt.fly.entity.User;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.req.SettleTimeAddReq;
import com.lt.fly.web.req.UpdateSettleTimeReq;
import com.lt.fly.web.vo.SettleTimeVo;

@RestController
@RequestMapping("/settleTime")
public class SettleTimeController extends BaseController{

	@Autowired
	private ISettleTimeRepository settleTimeRepository;
	@Autowired
	private IdWorker idwork;	
	@Autowired
	private IUserRepository iuserRepository;
	
	//添加一个统一结算时间
	@PostMapping("/add")
	public HttpResult<Object> add(@RequestBody @Validated SettleTimeAddReq req) throws ClientErrorException{
		
		SettleTime sTime=new SettleTime();
		sTime.setSettleTimeAll(req.getSettleTimeAll());
		sTime.setCreateTime(System.currentTimeMillis());
		sTime.setCreateUser(null);
		sTime.setId(idwork.nextId());
		sTime.setModifyTime(System.currentTimeMillis());
		Optional<User> op = iuserRepository.findById(this.getLoginUser().getId());
		if(!op.isPresent()) throw new ClientErrorException("未发现用户标识");			
		sTime.setModifyUser(op.get());
		sTime.setStatus(0);
		settleTimeRepository.save(sTime);				
		return HttpResult.success("添加成功");
	}
	@DeleteMapping("/{id}")
	public HttpResult<Object> deleteById(@PathVariable Long id) throws ClientErrorException{
		
		Optional<SettleTime> op = settleTimeRepository.findById(id);
		if(!op.isPresent()) throw new ClientErrorException("该条数据标识不存在");
		settleTimeRepository.deleteById(id); 				
		return HttpResult.success("删除成功！");
	}
	@PutMapping("/{id}")
	public HttpResult<Object> updateById(@PathVariable Long id,@RequestBody @Validated UpdateSettleTimeReq req) throws ClientErrorException{
		SettleTime op = isNotNull(settleTimeRepository.findById(id), "该条数据标识不存在");
		if(req.getSettleTimeAll()!=null || !req.getSettleTimeAll().equals("")) op.setSettleTimeAll(req.getSettleTimeAll());		
		op.setModifyTime(System.currentTimeMillis());
		User user = isNotNull(iuserRepository.findById(this.getLoginUser().getId()), "用户标识异常");
		op.setModifyUser(user);
		if(req.getStatus()!=null) {
			if(req.getStatus()==1) {
				 settleTimeRepository.updateStatusAll();				
				op.setStatus(req.getStatus());
			}
		}		
		SettleTime save = settleTimeRepository.save(op);
		return HttpResult.success(new SettleTimeVo(save),"修改成功");
	}
	@GetMapping
	public HttpResult<Object> get() throws ClientErrorException{
		
		List<SettleTime> list = settleTimeRepository.findAll();				
		return HttpResult.success(SettleTimeVo.toVo(list),"查询成功");
	}
	
}
