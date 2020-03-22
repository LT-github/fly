package com.lt.fly.web.controller;

import java.util.List;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IHandicapRepository;
import com.lt.fly.dao.IMemberRepository;
import com.lt.fly.entity.Handicap;
import com.lt.fly.entity.Member;
import com.lt.fly.entity.User;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.utils.ShareCodeUtil;
import com.lt.fly.web.query.MemberFind;
import com.lt.fly.web.req.MemberAddBySystem;
import com.lt.fly.web.query.MemberFindPage;
import com.lt.fly.web.req.MemberEditByClient;
import com.lt.fly.web.req.MemberEditBySystem;
import com.lt.fly.web.req.MemberTypeEdit;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.MemberFinanceVo;
import com.lt.fly.web.vo.MemberVo;
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
	public HttpResult<MemberVo> addMember(@RequestBody @Validated MemberAddBySystem obj ,
										  BindingResult bindingResult) throws ClientErrorException {
		this.paramsValid(bindingResult);
		Member member = new Member();
		member.setId(idWorker.nextId());
		member.setCreateTime(System.currentTimeMillis());
		BeanUtils.copyProperties(obj, member);
		if (null == obj.getNickname())
			member.setNickname(obj.getUsername());

		if (null != obj.getHandicapId()){
			Handicap handicap = isNotNull(iHandicapRepository.findById(obj.getHandicapId()),"组id查询不到实体");
			member.setHandicap(handicap);
			member.setIsHaveHandicap(GlobalConstant.IsHaveHandicap.YSE.getCode());
		}else {
			member.setIsHaveHandicap(GlobalConstant.IsHaveHandicap.NOT.getCode());
		}

		if (null != obj.getReferralCode()){
            Long memberId = ShareCodeUtil.codeToId(obj.getReferralCode());
            Member modifyUser = isNotNull(iMemberRepository.findById(memberId),"邀请码不正确!");
            member.setModifyUser(modifyUser);
		}

		member.setCreateTime(System.currentTimeMillis());
		member.setCreateUser(getLoginUser());

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
	public HttpResult<MemberVo> editStore(@PathVariable Long id , @RequestBody @Validated MemberEditBySystem obj ,
			BindingResult bindingResult) throws ClientErrorException{
		this.paramsValid(bindingResult);
		
		Member objEdit = isNotNull(iMemberRepository.findById(id),"会员id查询不到实体");
		BeanUtils.copyProperties(obj, objEdit);
		if (null == obj.getNickname())
			objEdit.setNickname(objEdit.getNickname());


		if (null != obj.getHandicapId()) {
			Handicap handicap = isNotNull(iHandicapRepository.findById(obj.getHandicapId()),"组id查询不到实体");
			objEdit.setHandicap(handicap);
			objEdit.setIsHaveHandicap(GlobalConstant.IsHaveHandicap.YSE.getCode());
		}else {
			objEdit.setIsHaveHandicap(GlobalConstant.IsHaveHandicap.NOT.getCode());
		}

		objEdit.setModifyUser(getLoginUser());
		objEdit.setModifyTime(System.currentTimeMillis());

		iMemberRepository.save(objEdit);
		
		return HttpResult.success(new MemberVo(objEdit), "编辑成功");
	}
	
	/**
	 * 查询所有会员-分页
	 * @param query
	 * @return
	 */
	@GetMapping
	@UserLoginToken
	public HttpResult findAllPage(MemberFindPage query){

		if (null != query.getHandicapId() && query.getHandicapId().equals(GlobalConstant.NoMemberHandicap.ID.getCode())){
			query.setHandicapId(null);
			query.setIsHaveHandicap(GlobalConstant.IsHaveHandicap.NOT.getCode());
		}

		Page<Member> page = iMemberRepository.findAll(query);
		PageResp<MemberFinanceVo, Member> prp = new PageResp<MemberFinanceVo, Member>(page);
		prp.setData(MemberFinanceVo.tovo(page.getContent()));

		return HttpResult.success(prp, "查询成功");
	}
	
	/**
	 * 查询所有会员,不分页
	 * @return
	 */
	@GetMapping("/all")
	@UserLoginToken
	public HttpResult findAllByList(MemberFind query){
		if (null != query.getHandicapId() && query.getHandicapId().equals(GlobalConstant.NoMemberHandicap.ID.getCode())){
			query.setHandicapId(null);
			query.setIsHaveHandicap(GlobalConstant.IsHaveHandicap.NOT.getCode());
		}
		List<Member> list = iMemberRepository.findAll(query);
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
	 *  按照id查询
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

	/**
	 * 更改会员类型
	 * @param id
	 * @param req
	 * @return
	 * @throws ClientErrorException
	 */
	@UserLoginToken
	@PutMapping("type/{id}")
	public HttpResult updateType(@PathVariable Long id,@RequestBody MemberTypeEdit req) throws ClientErrorException{

		Member member = isNotNull(iMemberRepository.findById(id),"会员id查询不到实体");
		member.setType(req.getType());

		String msg = null;
		if (req.getType().equals(GlobalConstant.MemberType.GENERAL.getCode())){
			msg = GlobalConstant.MemberType.GENERAL.getMsg();
		} else if (req.getType().equals(GlobalConstant.MemberType.REFERRER.getCode())){
			msg = GlobalConstant.MemberType.REFERRER.getMsg();
		} else {
			throw new ClientErrorException("会员类型不存在");
		}
		return HttpResult.success(new MemberVo(member),"更新"+member.getNickname()+"成为"+msg+"成功!");
	}

}
