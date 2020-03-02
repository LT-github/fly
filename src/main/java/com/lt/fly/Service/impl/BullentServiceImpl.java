package com.lt.fly.Service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lt.fly.Service.BaseService;
import com.lt.fly.Service.IBullentService;
import com.lt.fly.dao.IAdminRepository;
import com.lt.fly.dao.IBullentRepository;
import com.lt.fly.dao.IUserRepository;
import com.lt.fly.entity.Admin;
import com.lt.fly.entity.Bullent;
import com.lt.fly.entity.User;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.utils.MyBeanUtils;
import com.lt.fly.web.req.BullentUpdateStatus;
import com.lt.fly.web.req.BulletinAddReq;
import com.lt.fly.web.req.BulletinDeleteReq;
import com.lt.fly.web.req.FindBulletinsPageQo;
import com.lt.fly.web.req.FindUserBulletinsPageQo;
import com.lt.fly.web.req.UpdateBullentReq;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.resp.PageResp.PageGenerator;
import com.lt.fly.web.vo.BullentVo;


@Service
public class BullentServiceImpl extends BaseService implements IBullentService{

	
	@Autowired
	IBullentRepository bulletinRepository;
	@Autowired
	IdWorker idWorker;
	@Autowired
	IUserRepository userRepository;
	@Autowired
	IBullentService bulletinService;
	@Autowired
	IAdminRepository adminRepository;

	/*增加公告*/
	@Override
	public void add(BulletinAddReq req) throws ClientErrorException {


		if(req==null)
			throw new ClientErrorException("空请求！");
		if(req.getTitle()==null)
			throw new ClientErrorException("标题不能为空！");
		
		if(req.getStatus()==null || req.getStatus()<0)
			throw new ClientErrorException("请选择公告的状态！");
		if(req.getContent()==null)
			throw new ClientErrorException("公告内容不能为空！");

		long nextId = idWorker.nextId();
		List<User> u=null;
		Bullent bullent=new Bullent();	
		BeanUtils.copyProperties(req, bullent);	
		//保存草稿,即未推送
		if(req.getStatus()==0) {
			bullent.setId(nextId);
			bullent.setCreateTime(System.currentTimeMillis());
			//设置公告作者
			Optional<Admin> op = adminRepository.findById(this.getLoginUser().getId());
			if(!op.isPresent())
				throw new ClientErrorException("查无此作者！");
			Admin admin = op.get();			
			bullent.setCreateUser(admin);
			bulletinRepository.save(bullent);
			return;
		}
		
		
		if(req.getPushTime()==null || req.getEndTime()==null || req.getPushTime()==0 || req.getEndTime()==0)
			throw new ClientErrorException("请选择公告的推送时间区间！");
		if(req.getType()==null) 
			throw new ClientErrorException("请选择公告的类型！");
		
		if(req.getType()==0) {			
			u=userRepository.findAll();									
		}
		if(req.getType()==1) {
		       List<Long> userIds = req.getUserIds();
		   if( userIds==null|| userIds.size()<=0 )
			     throw new ClientErrorException("请选中要推送的用户！");								
		//公告状态 未推送 0 、已推送 1 、 作废 2																																																
		//根据传过来的用户ids,查出要推送公告的所有用户
		u = userRepository.findAllById(req.getUserIds());
		}
		
		bullent.setPushEndTime(req.getEndTime());
		//new一个set集合接受查出来的用户
		Set<User> users =new HashSet<User>(u);
		bullent.setId(nextId);			
		bullent.setCreateTime(System.currentTimeMillis());		
		//设置公告作者
		Optional<Admin> op = adminRepository.findById(this.getLoginUser().getId());
		if(!op.isPresent())
			throw new ClientErrorException("查无此作者！");
		Admin admin = op.get();
		bullent.setCreateUser(admin);
		//设置要推送的用户
		bullent.setPushUsers(users);
		//执行添加插入操作
		bulletinRepository.save(bullent);	

	}

	/*查询公告*/
	@Override
	public PageResp<BullentVo,Bullent> findBulletin(FindBulletinsPageQo req) throws ClientErrorException {

		if(req==null)
			throw new ClientErrorException("空请求！");

		//设置排序规则
		req.setPropertyName("createTime");
		req.setAscending(false);
		
		Page<Bullent> page = bulletinRepository.findAll(req);


		return new PageResp<BullentVo, Bullent>(page).getPageVo(new PageGenerator<BullentVo, Bullent>() {

			
			@Override
			public List<BullentVo> generator(List<Bullent> content)  {
				List<BullentVo> list = new ArrayList<BullentVo>();
				
				for(Bullent s : content) {
					List<Long> times=new ArrayList<Long>();
					BullentVo vo = new BullentVo();
					BeanUtils.copyProperties(s, vo);
					if(s.getPushTime()!=null && s.getPushEndTime()!=null)
					{ times.add(s.getPushTime());					
					  times.add(s.getPushEndTime());										
					  vo.setTime(times);
					}
					vo.setAuthorId(s.getCreateUser().getId());
					vo.setAuthorUsername(s.getCreateUser().getUsername());
					
					list.add(vo);
					
				}

				return list;
			}
		});								

	}

	@Override
	public Object deleteBulletin(BulletinDeleteReq req) throws ClientErrorException {
		if(req==null)
			throw new ClientErrorException("空请求！");
		if(req.getIds()==null || req.getIds().size()<=0)
			throw new ClientErrorException("请选中用户！");
		//根据ids查询所有公告
		List<Bullent> bulletins = bulletinRepository.findAllById(req.getIds());
		if(bulletins==null || bulletins.size()<=0)
			throw new ClientErrorException("没有查询到公告信息！");
		//执行删除
		bulletinRepository.deleteInBatch(bulletins);


		return null;
	}

	//客户端查自己的公告
	@Override
	public Object findUserBulletin(FindUserBulletinsPageQo req) throws ClientErrorException {


		Set<Bullent> bullents = this.getLoginUser().getReceiveBullents();

		if(bullents==null || bullents.size()<=0)
			throw new ClientErrorException("您还没有公告！或用户 "+this.getLoginUser().getUsername()+" 不存在！");

		List<BullentVo> list = new ArrayList<BullentVo>();
		for (Bullent s : bullents) {
			if(s.getStatus()==0 || s.getStatus()==2)
				continue;

			if(System.currentTimeMillis()>s.getPushEndTime()) {
				if(s.getStatus()!=2) 
				{
					s.setStatus(2);
					bulletinRepository.save(s);
				}
				continue;
			}

			BullentVo vo = new BullentVo();
			BeanUtils.copyProperties(s, vo);
			
			vo.setAuthorId(s.getCreateUser().getId());
			vo.setAuthorUsername(s.getCreateUser().getUsername());
			list.add(vo);
		}

		return list;

	}

	//改变公告状态	
	@Override
	public void updateStatus(BullentUpdateStatus req,Long id) throws ClientErrorException{

		Optional<Bullent> op = bulletinRepository.findById(id);
		if(!op.isPresent())
			throw new ClientErrorException("没有此条公告，或已被删除");

		Bullent bullent = op.get();

		BeanUtils.copyProperties(req, bullent);

		bulletinRepository.save(bullent);

	}

	//修改公告
	@Override
	public void updateBullent(UpdateBullentReq req) throws ClientErrorException {

		if(req.getStatus()==null || req.getStatus()<0)
			throw new ClientErrorException("请选择公告的状态或公告状态不合法！");
		if(req.getId()==null || req.getId()==0)
			throw new ClientErrorException("请选择公告id！");
		Bullent bullent = isNotNull(bulletinRepository.findById(req.getId()),"没有查询此条公告，或已被删除");


		//保存草稿,即未推送
		if(req.getStatus()==0) {
			MyBeanUtils.copyProperties(req, bullent);			
			bullent.setModifyTime(System.currentTimeMillis());
			bullent.setModifyUser(this.getLoginUser());
			bulletinRepository.save(bullent);

			return;
		}


		List<User> u =null;
		if(req.getStatus()==1) {


			if(req.getEndTime()==null || req.getPushTime()==null)
				throw new ClientErrorException("公告的推送时间不合法！");
			if(req.getPushTime()<=0 || req.getEndTime()<=0)
				throw new ClientErrorException("请选择公告的推送时间区间！");

			MyBeanUtils.copyProperties(req, bullent);

			bullent.setPushEndTime(req.getEndTime());
			if(req.getType()==null)
				throw new ClientErrorException("公告类型不能为空");
			
			if(req.getType()==0) {
				
				 u = userRepository.findAll();
			}
			if(req.getType()==1) {
			List<Long> userIds = req.getUserIds();
			if( userIds==null|| userIds.size()<=0 )
				throw new ClientErrorException("请选中要推送的用户！");	
			 u = userRepository.findAllById(req.getUserIds());
			
			}
			//new一个set集合接受查出来的用户
			Set<User> users =new HashSet<User>(u);						
			bullent.setModifyTime(System.currentTimeMillis());
			bullent.setModifyUser(this.getLoginUser());
			bullent.setStatus(req.getStatus());
			//设置要推送的用户
			bullent.setPushUsers(users);			
			Bullent save = bulletinRepository.save(bullent);
			if(save==null)
				throw new ClientErrorException("保存失败！");
		}



	}

}
