package com.lt.fly.web.controller;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.*;
import com.lt.fly.entity.*;
import com.lt.fly.utils.*;
import com.lt.fly.web.query.FinanceFind;
import com.lt.fly.web.query.ReturnPointFindPage;
import com.lt.fly.web.req.FinanceAdd;
import com.lt.fly.web.req.HandicapSettlementReq;
import com.lt.fly.web.req.ReturnSettle;
import com.lt.fly.web.req.ReturnSettleMulity;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.MemberVo;
import com.lt.fly.web.vo.ReturnPointVo;
import com.lt.fly.web.vo.ReturnPointVoByTime;
import com.lt.fly.web.vo.FinanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.google.common.collect.Lists;
import com.lt.fly.Service.IFinanceService;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.web.req.JudgeAuditFinanceReq;

import java.util.*;
import java.util.stream.Collectors;

import static com.lt.fly.utils.GlobalConstant.FinanceType.*;


@RestController
@RequestMapping("/finance")
public class FinanceController extends BaseController{

	
	@Autowired
	private IFinanceRepository iFinanceRepository;

	@Autowired
	private IFinanceService iFinanceService;

	@Autowired
	private IMemberRepository iMemberRepository;

	@Autowired
	private ISettingsRepository iSettingsRepository;

	/**
	 * 审核
	 * @param req
	 * @throws ClientErrorException
	 */
	@PostMapping("/judge")
	@UserLoginToken
	public HttpResult<Object> judge(@RequestBody @Validated JudgeAuditFinanceReq req) throws ClientErrorException {
		Finance finance = isNotNull(iFinanceRepository.findById(req.getAuditFinanceId()),"传递的参数不存在实体");
		MyBeanUtils.copyProperties(req,finance);
		if (!req.getAuditStatus().equals(GlobalConstant.AuditStatus.AUDIT_PASS.getCode())){
			finance.setCountType(GlobalConstant.CountType.SUBTRACT.getCode());
		}
		finance.setModifyTime(System.currentTimeMillis());
		finance.setModifyUser(getLoginUser());
		iFinanceRepository.save(finance);
		return HttpResult.success(new FinanceVo(finance),"审核成功！");
	}

	/**
	 * 财务列表
	 * @param query
	 * @return
	 * @throws ClientErrorException
	 */
	@GetMapping
	@UserLoginToken
	public HttpResult find(FinanceFind query) throws ClientErrorException{
		return HttpResult.success(iFinanceService.findAll(query),"查询财务列表成功!");
	}

	/**
	 * 可分红与回水列表
	 * @return
	 * @throws ClientErrorException
	 */
	@GetMapping("return")
	@UserLoginToken
	public HttpResult canReturnFind(ReturnPointFindPage query) throws ClientErrorException{

		query.setPage(query.getPage()-1);

		if(null == query.getFindType() || null == query.getType()){
			throw new ClientErrorException("参数不能为空");
		}

		if (!query.getType().equals(RANGE_LIUSHUI.getCode())
		&& !query.getType().equals(RANGE_YINGLI.getCode())
		&& !query.getType().equals(REFERRAL_LIUSHUI.getCode())
		&& !query.getType().equals(REFERRAL_YINGLI.getCode())){
			throw new ClientErrorException("参数错误");
		}

		//找到所有会员
		List<Member> members = iMemberRepository.findAll(query);

		List<ReturnPointVo> list = new ArrayList<>();
		if (query.getType().equals(REFERRAL_LIUSHUI.getCode()) || query.getType().equals(REFERRAL_YINGLI.getCode())) {
			members = members.stream()
					.filter((Member member) -> member.getType().equals(GlobalConstant.MemberType.REFERRER.getCode()))
					.collect(Collectors.toList());
		}

		for (Member item :
				members) {
			list.add(getReturnPointVo(query.getType(), item));
		}

		list = list.stream()
				.filter((ReturnPointVo vo) -> vo.getMoney() > 0)
				.collect(Collectors.toList());//过滤money<0或等于0的

		if (query.getFindType().equals(GlobalConstant.FindReturnType.CAN.getCode())){
			list = list.stream()
					.filter((ReturnPointVo vo) -> vo.getTime() == 0)
					.collect(Collectors.toList());
		} else if (query.getFindType().equals(GlobalConstant.FindReturnType.WAIT.getCode())){
			list = list.stream()
					.filter((ReturnPointVo vo) -> vo.getTime() > 0)
					.collect(Collectors.toList());
		}
		long size = (long) list.size();

		list = list.stream()
				.sorted(Comparator.comparing(ReturnPointVo::getMoney,Comparator.reverseOrder()).thenComparing(ReturnPointVo::getTime))//先按money降序,再按time升序
				.skip(query.getPage() * (query.getSize() - 1))//分页
				.limit(query.getSize())
				.collect(Collectors.toList());
		return HttpResult.success(new PageResp(query.getPage(), query.getSize(), (list.size()  +  query.getSize()  - 1) / query.getSize(), size, list),"获取待结算列表成功!");
	}

	/**
	 * 结算返点
	 * @param memberId
	 * @param req
	 * @param bindingResult
	 * @return
	 * @throws ClientErrorException
	 */
	@PutMapping("/return/{memberId}")
	@UserLoginToken
	public HttpResult settle(@PathVariable Long memberId, @RequestBody @Validated ReturnSettle req, BindingResult bindingResult) throws ClientErrorException{
		this.paramsValid(bindingResult);
		Finance finance = settleFinance(memberId, req);

		return HttpResult.success(new FinanceVo(finance),"结算成功");
	}



	/**
	 * 批量结算返点
	 * @param req
	 * @return
	 * @throws ClientErrorException
	 */
	@PostMapping
	@UserLoginToken
	public HttpResult settleMultiy(@RequestBody ReturnSettleMulity req) throws ClientErrorException{
		if (req.getIds().size() == 0 ){
			throw new ClientErrorException("请选择将要操作的用户");
		}
		for (Long id :
				req.getIds()) {
			Finance finance = settleFinance(id, req);
		}

		return HttpResult.success(null,"操作成功,共操作"+req.getIds().size()+"条数据");
	}


	/**
	 * 结算返点
	 * @param memberId
	 * @param req
	 * @return
	 * @throws ClientErrorException
	 */
	private Finance settleFinance(@PathVariable Long memberId, @Validated @RequestBody ReturnSettle req) throws ClientErrorException {
		GlobalConstant.FinanceType type = GlobalConstant.FinanceType.getFinanceTypeByCode(req.getType());


		Long time = getReturnTime(memberId,req.getType());
		if (!time.equals(0l)){
			Member member = isNotNull(iMemberRepository.findById(memberId),"传递的memberId没有实体");
			throw new ClientErrorException("据下次结算"+member.getUsername()+"的"+type.getMsg()+"还有"+ DateUtil.formatDateTime(time));
		}

		Member member = isNotNull(iMemberRepository.findById(memberId),"无此用户");

		ReturnPointVo vo = getReturnPointVo(req.getType(), member);
		Finance finance = iFinanceService.add(member,vo.getReturnMoney(),iFinanceService.reckonBalance(memberId), type);

		finance.setModifyUser(getLoginUser());
		finance.setModifyTime(System.currentTimeMillis());
		iFinanceRepository.save(finance);
		return finance;
	}



	/**
	 * 系统上分
	 * @param id
	 * @param req
	 * @return
	 * @throws ClientErrorException
	 */
	@PutMapping("recharge/{id}")
	@UserLoginToken
	public HttpResult recharge(@PathVariable Long id,@RequestBody FinanceAdd req) throws ClientErrorException{
		Member member = isNotNull(iMemberRepository.findById(id),"传递的会员id没有实体");
		Finance finance = iFinanceService.add(member,req.getMoney(),null, SYSTEM_RECHARGE);
		return HttpResult.success(new MemberVo(member),"给"+member.getUsername()+"上分"+req.getMoney()+"成功");
	}

	/**
	 * 系统下分
	 * @param id
	 * @param req
	 * @return
	 * @throws ClientErrorException
	 */
	@PutMapping("descend/{id}")
	@UserLoginToken
	public HttpResult descend(@PathVariable Long id,@RequestBody FinanceAdd req) throws ClientErrorException{
		Member member = isNotNull(iMemberRepository.findById(id),"传递的会员id没有实体");
		//先查余额
		double balance = iFinanceService.reckonBalance(member.getId());
		if(balance<req.getMoney()){
			throw new ClientErrorException("余额不足");
		}
		Finance finance = iFinanceService.add(member,req.getMoney(),balance, SYSTEM_DESCEND);
		iFinanceRepository.save(finance);
		return HttpResult.success(new MemberVo(member),"给"+member.getUsername()+"下分"+req.getMoney()+"成功");
	}

	/**
	 * 会员用户的返点Vo
	 */
	private ReturnPointVo getReturnPointVo(Integer type, Member member) throws ClientErrorException {

		ReturnPointVo vo = new ReturnPointVo();
		double returnPoint = 0;

		double money = 0;

		//上次结算财务记录
		Finance last = iFinanceRepository.findNew(type,member.getId());


		//找到返点值
		Handicap handicap = member.getHandicap();
		Set<Proportion> proportions = null;
		//普通会员与推手会员的返点
		if (type.equals(REFERRAL_LIUSHUI.getCode()) || type.equals(REFERRAL_YINGLI.getCode())) {
			proportions = member.getProportions();
			money = getAllMoney(type,member,last);
		} else {
			money = getMoney(type, member, last);
			if (null != handicap) {
				proportions = handicap.getProportions();
			}
		}

		if (null != proportions && 0 != proportions.size()){
			for (Proportion proportion :
					proportions) {
				if (type.equals(RANGE_LIUSHUI.getCode())) {
					returnPoint = getReturnPoint(money, proportion, CommonsUtil.RANGE_LIUSHUI_RETURN_POINT);
				}
				if (type.equals(RANGE_YINGLI.getCode())){
					returnPoint = getReturnPoint(money, proportion, CommonsUtil.RANGE_YINGLI_RETURN_POINT);
				}
				if (returnPoint != 0){
					break;
				}

			}
		}

		vo.setMoney(money);
		vo.setUsername(member.getUsername());
		vo.setNikename(member.getNickname());
		vo.setReturnMoney(Arith.mul(money,returnPoint));
		vo.setMemberId(member.getId());
		vo.setTime(getReturnTime(member.getId(),type));
		return vo;
	}

	/*
	普通会员的返点金额
	 */
	private double getMoney(Integer type, Member member, Finance last) {
		double money = 0;
		List<Finance> finances;
		if (null != last){
			finances = iFinanceRepository.findByCreateUserAndCreateTimeAfter(member,last.getCreateTime());
		}else {
			finances = iFinanceRepository.findByCreateUser(member);
		}

		if (null != finances && 0 != finances.size()){
			money =  Arith.sub(iFinanceService.getReduce(new HashSet<>(finances), BET_RESULT),
					Arith.sub(iFinanceService.getReduce(new HashSet<>(finances), BET),iFinanceService.getReduce(new HashSet<>(finances), BET_CANCLE)));
			if (money < 0) {
				return Arith.round(Math.abs(money),2);
			}
		}
		return 0;
	}

	/*
	推手会员的返点金额
	 */
	private double getAllMoney(Integer type, Member member, Finance last) {
		double money = 0;
		List<Member> members = iMemberRepository.findByModifyUser(member);
		for (Member item :
				members) {
			money = Arith.add(money,getMoney(type,item,last));
		}
		return money;
	}


	/*
	 * 获取返点比例
	 */
	private double getReturnPoint(double money, Proportion proportion, Long returnPointId) {
		double returnPoint = 0;
		if (proportion.getReturnPoint().getId().equals(returnPointId)) {
			String[] range = proportion.getRanges().split("-");
			if (money > Double.parseDouble(range[0]) && money < Double.parseDouble(range[1])) {
				returnPoint = Arith.div(proportion.getProportionVal(), 100, 2);
			}
		}
		return returnPoint;
	}

	/**
	 * 获取待结算剩余时间
	 */
	private Long getReturnTime(Long memberId,Integer type) throws ClientErrorException {
		Settings settings = null;
		if (type.equals(RANGE_LIUSHUI.getCode())) {
			settings = isNotNull(iSettingsRepository.findById(CommonsUtil.TUISHUI_TIME),"结算剩余时间获取错误");
		}else if (type.equals(RANGE_YINGLI.getCode())){
			settings = isNotNull(iSettingsRepository.findById(CommonsUtil.FENHONG_TIME),"结算剩余时间获取错误");
		} else if (type.equals(REFERRAL_LIUSHUI.getCode())) {
			settings = isNotNull(iSettingsRepository.findById(CommonsUtil.REFERRAL_TUISHUI_TIME),"结算剩余时间获取错误");
		} else if (type.equals(REFERRAL_YINGLI.getCode())) {
			settings = isNotNull(iSettingsRepository.findById(CommonsUtil.REFERRAL_FENHONG_TIME),"结算剩余时间获取错误");
		} else {
			throw new ClientErrorException("结算剩余时间获取错误");
		}
		String hour = settings.getDataValue();
		long time = Long.parseLong(hour)*60*60*1000;

		Finance last = iFinanceRepository.findNew(type,memberId);
		if (null != last){
			long difference = System.currentTimeMillis()-last.getCreateTime();
			if (time-difference>0) {
				return time-difference;
			}
		}
		return 0l;
	}
	@Autowired
	private IHandicapRepository handicapRepository;
	/*
	普通会员按时间的返点金额
	 */
	private double getMoneyByTime(Integer type, Member member, Finance last,Long settleTime) throws ClientErrorException {
		double money = 0;
		List<Finance> finances;
		if (null != last){
			finances = iFinanceRepository.findByCreateUserAndCreateTimeGreaterThanEqualAndCreateTimeLessThan(member,last.getCreateTime(),settleTime);
		}else {
			finances = iFinanceRepository.findByCreateUserAndCreateTimeBefore(member,settleTime);
		}	
		if(last.getCreateTime()>=settleTime) throw new ClientErrorException("该盘口按时间已结算");
			if (null != finances && 0 != finances.size()){
				if (null != finances && 0 != finances.size()){
					money =  Arith.sub(iFinanceService.getReduce(new HashSet<>(finances), BET_RESULT),
							Arith.sub(iFinanceService.getReduce(new HashSet<>(finances), BET),iFinanceService.getReduce(new HashSet<>(finances), BET_CANCLE)));
					if (money < 0) {
						return Arith.round(Math.abs(money),2);
					}
				}
			}
					
		return 0;
	}
	
	/*
	推手会员按时间的返点金额
	 */
	private double getAllMoneyByTime(Integer type, Member member, Finance last,Long settleTime) throws ClientErrorException{
		double money = 0;
		List<Member> members = iMemberRepository.findByModifyUser(member);
		for (Member item :
				members) {
			money = Arith.add(money,getMoneyByTime(type,item,last,settleTime));
		}
		return money;
	}
	private ReturnPointVoByTime getReturnPointVoByTime(Integer type, Member member,Long settleTime) throws ClientErrorException{
		
		ReturnPointVoByTime vo = new ReturnPointVoByTime();
		double returnPoint = 0;
		double money = 0;
		//上次结算财务记录
		Finance last = iFinanceRepository.findNew(type,member.getId());
      // if(last!=null) {}
		
		//找到返点值
		Handicap handicap = member.getHandicap();
		Set<Proportion> proportions = null;
		//普通会员与推手会员的返点
		if (type.equals(SETTLEMENT_TYPE_HAND.getCode())) {
			proportions = member.getProportions();
			money = getAllMoneyByTime(type,member,last,settleTime);
		} else {
			money = getMoneyByTime(type, member, last,settleTime);
			if(handicap!=null)
			proportions = handicap.getProportions();
		}
		if(proportions!=null) {
		for (Proportion proportion :
				proportions) {
			if (type.equals(RANGE_LIUSHUI.getCode())) {
				returnPoint = getReturnPoint(money, proportion, CommonsUtil.RANGE_LIUSHUI_RETURN_POINT);
			}	
			if (returnPoint != 0){
				break;
			}
		 }
		}
		vo.setMoney(money);
		vo.setUsername(member.getUsername());
		vo.setNikename(member.getNickname());
		vo.setReturnMoney(Arith.mul(money,returnPoint));
		vo.setMemberId(member.getId());
		vo.setTime(settleTime);
		return vo;
		
		
	}
	     
	    //手动结算
	   @PostMapping("/addByTime")
		public HttpResult<Object> addTime(@Validated @RequestBody HandicapSettlementReq req) throws ClientErrorException {
			
			List<Handicap> handicaps=Lists.newArrayList();
			List<Finance> fi=Lists.newArrayList();
			GlobalConstant.FinanceType type = GlobalConstant.FinanceType.getFinanceTypeByCode(req.getSettlementType());
			
			if(null==req.getHandicapIds()) { handicaps = handicapRepository.findAll();}else {handicaps = handicapRepository.findAllById(req.getHandicapIds());}
			if(handicaps== null || handicaps.size()==0) throw new ClientErrorException("暂时无任何盘口");
			for (Handicap handicap : handicaps) {
				Set<Member> members = handicap.getMembers();
				if(members==null || members.size()==0) continue;
				for (Member member : members) {																			
					ReturnPointVoByTime vo = getReturnPointVoByTime(req.getSettlementType(), member,req.getSettlementTime()); 					
					Finance finance = iFinanceService.add(member,vo.getReturnMoney(),iFinanceService.reckonBalance(member.getId()), type);
					finance.setModifyUser(getLoginUser());
					finance.setModifyTime(System.currentTimeMillis());
					iFinanceRepository.save(finance);
					fi.add(finance);
				}
			}
			
			return HttpResult.success(fi,"按时间结算成功!");
		}
	  
}
