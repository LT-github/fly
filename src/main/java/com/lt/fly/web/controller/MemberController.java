package com.lt.fly.web.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IHandicapRepository;
import com.lt.fly.dao.IMemberRepository;
import com.lt.fly.dao.IProportionRepository;
import com.lt.fly.entity.Finance;
import com.lt.fly.entity.Handicap;
import com.lt.fly.entity.Member;
import com.lt.fly.entity.Proportion;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.utils.*;
import com.lt.fly.web.query.MemberFind;
import com.lt.fly.web.req.MemberAddBySystem;
import com.lt.fly.web.query.MemberFindPage;
import com.lt.fly.web.req.MemberEditBySystem;
import com.lt.fly.web.req.MemberTypeEdit;
import com.lt.fly.web.req.ReferrerEdit;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.MemberFinanceVo;
import com.lt.fly.web.vo.MemberVo;
import com.lt.fly.web.vo.ProportionVo;
import com.lt.fly.web.vo.ReferrerMemberVo;
import org.apache.dubbo.remoting.Client;
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

	@Autowired
	private IProportionRepository iProportionRepository;
	
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

		existsForName(iMemberRepository.findByUsername(obj.getUsername()),"会员名已经存在");
		Member member = new Member();
		member.setId(idWorker.nextId());
		member.setCreateTime(System.currentTimeMillis());
		BeanUtils.copyProperties(obj, member);
		if (null == obj.getNickname() || obj.getNickname().isEmpty())
			member.setNickname(obj.getUsername());

		if (null != obj.getHandicapId() && !obj.getHandicapId().equals("")){
			Handicap handicap = isNotNull(iHandicapRepository.findById(obj.getHandicapId()),"组id查询不到实体");
			member.setHandicap(handicap);
			member.setIsHaveHandicap(GlobalConstant.IsHaveHandicap.YSE.getCode());
		}else {
			member.setIsHaveHandicap(GlobalConstant.IsHaveHandicap.NOT.getCode());
		}

		//设置推荐人
		if (null != obj.getReferralCode() && !obj.getReferralCode().isEmpty()){
            Member modifyUser = iMemberRepository.findByReferralCode(obj.getReferralCode());
            if (null == modifyUser){
            	throw new ClientErrorException("邀请码不正确");
			}
            if (!modifyUser.getType().equals(GlobalConstant.MemberType.REFERRER.getCode())){
            	throw new ClientErrorException("邀请码为'"+obj.getReferralCode()+"'的用户不是推手");
			}
            member.setModifyUser(modifyUser);
		}

		//设置推手会员
		if (obj.getType().equals(GlobalConstant.MemberType.REFERRER.getCode())) {
			member.setReferralCode(ShareCodeUtil.genInviteCode(member.getId(),6));
		}
		eidtProportion(member, obj.getProportionIds(), obj.getType());

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

		// 设置返点
		eidtProportion(objEdit, obj.getProportionIds(), obj.getType());

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
		//设置推手会员
		if (req.getType().equals(GlobalConstant.MemberType.REFERRER.getCode())) {
			member.setReferralCode(ShareCodeUtil.genInviteCode(member.getId(),6));
		}


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


	/**
	 * 推手列表管理
	 * @return
	 * @throws ClientErrorException
	 */
	@UserLoginToken
	@GetMapping("referrer")
	public HttpResult findReferrer(MemberFindPage query) throws ClientErrorException{
		//设置为推手会员
		query.setType(GlobalConstant.MemberType.REFERRER.getCode());
		Page<Member> page = iMemberRepository.findAll(query);
		PageResp resp = new PageResp(page);
		List<ReferrerMemberVo> referralMemberVos = new ArrayList<>();

		//总流水
		double waterTotal = 0;
		//总盈亏
		double betResultTotal = 0;
		//总分红
		double dividendTotal = 0;

		for (Member item :
				page.getContent()) {
			ReferrerMemberVo vo = new ReferrerMemberVo();
			if (null != item.getModifyUser()){
				vo.setReferralName(item.getModifyUser().getUsername());
			}
			vo.setId(item.getId());
			vo.setMemberName(item.getUsername());
			vo.setNickName(item.getNickname());
			vo.setReferralCode(item.getReferralCode());
			vo.setStatus(item.getStatus());
			if (null != item.getProportions() && 0 !=item.getProportions().size()){
				vo.setProportionVos(ProportionVo.toVo(new ArrayList<>(item.getProportions())));
			}
			//被推荐的用户
			List<Member> members = iMemberRepository.findByModifyUser(item);
			if (null != members && 0 != members.size()){
				vo.setReferralNumber(members.size());
				for (Member member:
					 	members) {
					if (null != member.getFinances() && 0 != member.getFinances().size()) {
						for (Finance finance :
								member.getFinances()) {
							if (finance.getType().equals(GlobalConstant.FinanceType.BET.getCode())){
								waterTotal = Arith.add(waterTotal,finance.getMoney());
								betResultTotal = Arith.sub(betResultTotal,finance.getMoney());
							}
							if (finance.getType().equals(GlobalConstant.FinanceType.CANCLE.getCode())){
								waterTotal = Arith.sub(waterTotal,finance.getMoney());
							}
							if (finance.getType().equals(GlobalConstant.FinanceType.BET_WIN.getCode())){
								betResultTotal = Arith.add(betResultTotal,finance.getMoney());
							}
						}
					}
				}
			}
			if (null != item.getFinances() && 0 != item.getFinances().size()) {
				for (Finance finance :
						item.getFinances()) {
					if (finance.getType().equals(GlobalConstant.FinanceType.REFERRAL_LIUSHUI.getCode())
							|| finance.getType().equals(GlobalConstant.FinanceType.REFERRAL_YINGLI.getCode())){
						dividendTotal = Arith.add(dividendTotal,finance.getMoney());
					}
				}
			}
			vo.setBetResultTotal(betResultTotal);
			vo.setDividendTotal(dividendTotal);
			vo.setWaterTotal(waterTotal);
			referralMemberVos.add(vo);
		}
		resp.setData(referralMemberVos);
		return HttpResult.success(resp,"获取推手列表成功!");
	}

	/**
	 * 被推荐的会员列表
	 * @param query
	 * @return
	 * @throws ClientErrorException
	 */
	@GetMapping("referrer/{id}")
	@UserLoginToken
	public HttpResult findReferrerAll(@PathVariable Long id,DataQueryObjectPage query) throws ClientErrorException{
		Member referrer = isNotNull(iMemberRepository.findById(id),"传递的参数未找到实体");
		List<Member> members= iMemberRepository.findByModifyUser(referrer);
		long count = members.stream().count();
		int totalPageNum = (int)(count  +  query.getSize()  - 1) / query.getSize();//计算总页数
		List<Member> memberList = members.stream().limit(query.getSize()).skip(query.getPage()).collect(Collectors.toList());

		PageResp resp = new PageResp(query.getPage(), query.getSize(), totalPageNum, count, MemberFinanceVo.tovo(memberList));

		return HttpResult.success(resp,"获取推荐详情列表成功!");
	}


	/**
	 * 推手详情页
	 * @param id
	 * @param req
	 * @return
	 * @throws ClientErrorException
	 */
	@PutMapping("referrer/{id}")
	@UserLoginToken
	public HttpResult putProportion(@PathVariable Long id, @RequestBody ReferrerEdit req) throws ClientErrorException {
		Member member = isNotNull(iMemberRepository.findById(id),"传递的推手id不存在实体");
		if (null == member.getType() || member.getType().equals(GlobalConstant.MemberType.GENERAL.getCode())) {
			throw new ClientErrorException("此会员不是推手会员");
		}
		return null;
	}

	// 设置返点
	private void eidtProportion(Member member, List<Long> proportionIds2, Integer type) throws ClientErrorException {
		if (null != proportionIds2 && 0 != proportionIds2.size()) {
			if (type.equals(GlobalConstant.MemberType.GENERAL.getCode())) {
				throw new ClientErrorException("普通会员无权设置返点!");
			}
			List<Long> proportionIds = proportionIds2;
			Set<Proportion> pro = new HashSet<>();
			for (Long proportionId : proportionIds) {
				Proportion proportion = isNotNull(iProportionRepository.findById(proportionId), "添加的返点不存在");
				pro.add(proportion);
			}
			member.setProportions(pro);
		}
	}
}
