package com.lt.fly.web.controller;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IAdminRepository;
import com.lt.fly.dao.IAuthorityRepository;
import com.lt.fly.entity.Authority;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.log.Log;
import com.lt.fly.web.req.AuthorityAdd;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.AuthorityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/authority")
public class AuthorityController extends BaseController{

	@Autowired
	private IAuthorityRepository iAuthorityRepository;
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private IAdminRepository iAdminRepository;
	
	/**
	 *  添加一个权限，但不做角色的关联、角色和权限的关联放到添加角色时添加
	 * @param obj
	 * @param bindingResult
	 * @return
	 * @throws ClientErrorException
	 */
//	@RequiredPermission(value="addAuthority")
	@Log(value = "添加权限")
	@PostMapping
	@UserLoginToken
	public HttpResult<AuthorityVo> addAuthority(@RequestBody @Validated AuthorityAdd obj ,
												BindingResult bindingResult) throws ClientErrorException {
		
		this.paramsValid(bindingResult);

		existsForName(iAuthorityRepository.findByName(obj.getName()),"权限名已经存在");

		Authority auth = addOrEditAuth(obj,null);
		iAuthorityRepository.save(auth);
		return HttpResult.success(new AuthorityVo(auth),auth.getName() + "添加成功");
	}
	
	/**
	 * 修改权限基本信息、关联的父元素
	 * @param id
	 * @param obj
	 * @param bindingResult
	 * @return
	 * @throws ClientErrorException
	 */
//	@RequiredPermission(value="editAuthority")
	@Log(value = "修改权限信息")
	@PutMapping("/{id}")
	@UserLoginToken
	public HttpResult<AuthorityVo> editAuthority(@PathVariable Long id,@RequestBody @Validated AuthorityAdd obj ,
			BindingResult bindingResult) throws ClientErrorException{
		
		this.paramsValid(bindingResult);
		
		Authority auth = addOrEditAuth(obj,id);
		iAuthorityRepository.flush();
		return HttpResult.success(new AuthorityVo(auth),auth.getName() + "修改成功");
	}
	
	/**
	 * 删除一个权限
	 * @param id
	 * @return
	 * @throws ClientErrorException
	 */
//	@RequiredPermission(value="deleteAuthority")
	@Log(value = "删除权限")
	@DeleteMapping("/{id}")
	@UserLoginToken
	public HttpResult<Object> deleteAuthority(@PathVariable Long id)
			throws ClientErrorException{
		
		Optional<Authority> optional = iAuthorityRepository.findById(id);
		if(!optional.isPresent()){
			throw new ClientErrorException("修改的id找不到实体");
		}
		Authority auth = optional.get();
		
		
		auth.getRoles().clear();
		//iauthorityRepository.save(auth);
		iAuthorityRepository.delete(auth);;
		return HttpResult.success(new AuthorityVo(auth), "删除权限" + auth.getName() + "成功");
	}
	
	/**
	 * 条件查询所有的权限
	 * @param query
	 * @return
	 */
//	@RequiredPermission(value="findAllAuthority")
	@Log(value = "查询权限列表")
	@GetMapping("/all")
	@UserLoginToken
	public HttpResult<PageResp<AuthorityVo,Authority>> findAll(DataQueryObjectPage query){
		
		Page<Authority> page = iAuthorityRepository.findAll(query);
		
		PageResp<AuthorityVo,Authority> res = new PageResp<>(page);
		res.setData(AuthorityVo.toVo(page.getContent()));
		
		return HttpResult.success(res,"查询成功" );
	}
	

	private Authority addOrEditAuth(AuthorityAdd obj,Long id) throws ClientErrorException {
		// 设置基本属性
		Authority auth = null;
		if(id!=null) {
			Optional<Authority> optional = iAuthorityRepository.findById(id);
			if(!optional.isPresent()){
				throw new ClientErrorException("修改的id找不到实体");
			}
			auth = optional.get();
			
		}else {
			auth = new Authority();
			auth.setId(idWorker.nextId());
			auth.setCreateTime(System.currentTimeMillis());
		}
		auth.setName(obj.getName());
		auth.setDescription(obj.getDescription());
		auth.setIdentifier(obj.getIdentifier());
		
		// 设置父元素
		if(obj.getParentId() != null) {
			Optional<Authority> optional = iAuthorityRepository.findById(obj.getParentId());
			if(!optional.isPresent())
				throw new ClientErrorException("权限父元素id查询不到实体");
			
			Authority parent = optional.get();
			auth.setParent(parent);
		}
		return auth;
	}
	
//	@RequiredPermission(value="findAllAuthorityList")
	@GetMapping("/list")
	public HttpResult<List<AuthorityVo>> list(){
		return HttpResult.success(AuthorityVo.toVo(iAuthorityRepository.findAll()),"权限列表或去成功");
	}
	
}
