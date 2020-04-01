package com.lt.fly.web.controller;

import java.util.List;
import java.util.Optional;

import com.lt.fly.annotation.RequiredPermission;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IAuthorityRepository;
import com.lt.fly.dao.IRoleRepository;
import com.lt.fly.entity.Authority;
import com.lt.fly.entity.Member;
import com.lt.fly.entity.Role;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.jpa.support.DataQueryObjectSort;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.req.RoleAdd;
import com.lt.fly.web.req.RoleQueryReq;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.MemberFinanceVo;
import com.lt.fly.web.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Autowired
	private IRoleRepository iRoleRepository;
	
	@Autowired
	private IAuthorityRepository iAuthorityRepository;
	
	@Autowired
	private IdWorker idWorker;
	
	/**
	 * 添加一个角色、并且给角色添加权限
	 * @param obj
	 * @param bindingResult
	 * @return
	 * @throws ClientErrorException
	 */
//	@RequiredPermission(value = "addRole")
	@PostMapping
	@UserLoginToken
	public HttpResult<RoleVo> addRole(@RequestBody @Validated RoleAdd obj ,
									  BindingResult bindingResult) throws ClientErrorException {
		this.paramsValid(bindingResult);

		existsForName(iRoleRepository.findByName(obj.getName()),"角色名已经存在");

		Role role = addOrEditRole(obj, null);
		iRoleRepository.save(role);
		return HttpResult.success(new RoleVo(role), "角色" + role.getName() +"添加成功");
	}
	
	/**
	 * 修改角色信息、权限
	 * @param id
	 * @param obj
	 * @param bindingResult
	 * @return
	 * @throws ClientErrorException
	 */
//	@RequiredPermission(value = "editRole")
	@PutMapping("/{id}")
	@UserLoginToken
	public HttpResult<RoleVo> editRole(@PathVariable Long id,@RequestBody @Validated RoleAdd obj , 
			BindingResult bindingResult) throws ClientErrorException{

		this.paramsValid(bindingResult);
		Role role = iRoleRepository.findByName(obj.getName());
		if (role != null && !role.getId().equals(id)){
			throw new ClientErrorException("角色名已经存在");
		}

		role = addOrEditRole(obj, id);
		iRoleRepository.flush();
		return HttpResult.success(new RoleVo(role), "角色" + role.getName() +"添加成功");
	}
	
	/**
	 * 删除角色
	 * @param id
	 * @return
	 * @throws ClientErrorException
	 */
//	@RequiredPermission(value = "deleteRole")
	@DeleteMapping("/{id}")
	@UserLoginToken
	public HttpResult<Object> deleteRole(@PathVariable Long id) 
			throws ClientErrorException{
		
		Optional<Role> optional = iRoleRepository.findById(id);
		if(!optional.isPresent())
			throw new ClientErrorException("删除的角色id查询不到实体");
		Role role = optional.get();
		role.getAuthoritys().clear();
		iRoleRepository.delete(role);
		
		return HttpResult.success(null, "角色["+role.getName()+"]删除成功");
	}
	
	/**
	 * 条件查询所有角色
	 * @param query
	 * @return
	 */
//	@RequiredPermission(value = "findAllRole")
	@GetMapping("/all")
	@UserLoginToken
	public HttpResult<Object> findAll(RoleQueryReq query){
		 Page<Role> page = iRoleRepository.findAll(query);
		 PageResp<RoleVo, Role> prp = new PageResp<RoleVo, Role>(page);
			prp.setData(RoleVo.toVo(page.getContent()));
		return HttpResult.success(prp, "查询成功");
	}
	
	private Role addOrEditRole(RoleAdd obj , Long id) throws ClientErrorException {
		
		// 设置基本信息
		Role role = null;
		if(id != null) {
			
			Optional<Role> optional = iRoleRepository.findById(id);
			if(!optional.isPresent())
				throw new ClientErrorException("修改的角色id查询不到实体");
			role = optional.get();
		}else {
			role = new Role();
			role.setId(idWorker.nextId());
			role.setCreateTime(System.currentTimeMillis());
		}
		
		role.setDescription(obj.getDescription());
		role.setName(obj.getName());
		role.getAuthoritys().clear();

		if (null != obj.getAuthIds() && 0 != obj.getAuthIds().size()){
			// 设置角色权限
			List<Long> authIds = obj.getAuthIds();
			for(Long authId : authIds) {
				Optional<Authority> optional = iAuthorityRepository.findById(authId);
				if(!optional.isPresent())
					throw new ClientErrorException("权限id查询不到实体");
				Authority authority = optional.get();
				role.getAuthoritys().add(authority);
			}
		}

		return role;
	}
	
	
}
