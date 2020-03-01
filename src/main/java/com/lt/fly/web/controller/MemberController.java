package com.lt.fly.web.controller;

import java.util.List;
import java.util.Optional;

import com.lt.fly.annotation.RequiredPermission;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IHandicapRepository;
import com.lt.fly.dao.IMemberRepository;
import com.lt.fly.entity.Handicap;
import com.lt.fly.entity.Member;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.req.MemberAdd;
import com.lt.fly.web.req.MemberEdit;
import com.lt.fly.web.req.MemberFind;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.MemberVo;
import org.apache.catalina.Store;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/member")
public class MemberController extends BaseController{
	
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private IHandicapRepository iHandicapRepository;
	
	@Autowired
	private IMemberRepository iMemberRepository;
	
	/**
	 *  添加会员
	 * @param obj
	 * @param bindingResult
	 * @return
	 * @throws ClientErrorException
	 */
	@PostMapping
	@UserLoginToken
	public HttpResult<MemberVo> addMember(@RequestBody @Validated MemberAdd obj ,
										  BindingResult bindingResult) throws ClientErrorException {
		this.paramsValid(bindingResult);
		
		Member member = new Member();
		member.setId(idWorker.nextId());
		member.setCreateTime(System.currentTimeMillis());
		member.setCreateUser(this.getLoginUser());
		BeanUtils.copyProperties(obj, member);
		if (null == obj.getNickname())
			member.setNickname(obj.getUsername());

		Handicap handicap = isNotNull(iHandicapRepository.findById(obj.getHandicapId()),"组id查询不到实体");
		member.setHandicap(handicap);

		iMemberRepository.save(member);
		
		return HttpResult.success(new MemberVo(member), "添加成功");
	}
	
	/**
	 * 编辑会员信息
	 * @param id
	 * @param obj
	 * @param bindingResult
	 * @return
	 * @throws ClientErrorException
	 */
	@PutMapping("/{id}")
	@UserLoginToken
	public HttpResult<MemberVo> editStore(@PathVariable Long id , @RequestBody @Validated MemberEdit obj ,
			BindingResult bindingResult) throws ClientErrorException{
		this.paramsValid(bindingResult);
		
		Member objEdit = isNotNull(iMemberRepository.findById(id),"会员id查询不到实体");
		BeanUtils.copyProperties(obj, objEdit);
		if (null == obj.getNickname())
			objEdit.setNickname(objEdit.getNickname());

		Handicap handicap = isNotNull(iHandicapRepository.findById(obj.getHandicapId()),"组id查询不到实体");
		objEdit.setHandicap(handicap);

		iMemberRepository.save(objEdit);
		
		return HttpResult.success(new MemberVo(objEdit), "编辑成功");
	}
	
	/**
	 * 查询所有会员-分页
	 * @param query
	 * @return
	 */
	@GetMapping("/all")
	@UserLoginToken
	public HttpResult<PageResp<MemberVo, Member>> findAllPage(MemberFind query){
		
		Page<Member> page = iMemberRepository.findAll(query);
		PageResp<MemberVo, Member> prp = new PageResp<MemberVo, Member>(page);
		prp.setData(MemberVo.toVo(page.getContent()));
		
		return HttpResult.success(prp, "查询成功");
	}
	
	/**
	 * 查询所有会员
	 * @return
	 */
	@GetMapping("/list")
	@UserLoginToken
	public HttpResult<List<MemberVo>> findAllByList(){
		List<Member> list = iMemberRepository.findAll();
		return HttpResult.success(MemberVo.toVo(list), "查询成功");
	}
	
	/**
	 * 停封会员
	 * @param id
	 * @return
	 * @throws ClientErrorException
	 */
	@DeleteMapping("/{id}")
	@UserLoginToken
	public HttpResult<Object> deleteStore(@PathVariable Long id) throws ClientErrorException{
		Member member = isNotNull(iMemberRepository.findById(id),"会员id查询不到实体");

		member.setStatus(GlobalConstant.UserStatus.PROHIBIT.getCode());
		iMemberRepository.save(member);
		
		return HttpResult.success(new MemberVo(member), "会员" + member.getNickname() + "被停封");
	}
	
	/**
	   *   按照id查询
	 * @param id
	 * @return
	 * @throws ClientErrorException
	 */
	@GetMapping("/{id}")
	@UserLoginToken
	public HttpResult<MemberVo> findById(@PathVariable Long id)throws ClientErrorException{

		Member member = isNotNull(iMemberRepository.findById(id),"会员id查询不到实体");
		
		return HttpResult.success(new MemberVo(member), "查询成功");
	}
}
