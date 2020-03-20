package com.lt.fly.web.controller;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.*;
import com.lt.fly.entity.*;
import com.lt.fly.utils.*;
import com.lt.fly.web.query.FinanceFind;
import com.lt.fly.web.query.ReturnPointFindPage;
import com.lt.fly.web.req.ReturnSettle;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.ReturnPointVo;
import com.lt.fly.web.vo.FinanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.lt.fly.Service.IFinanceService;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.web.req.JudgeAuditFinanceReq;

import java.util.*;

import static com.lt.fly.utils.GlobalConstant.FananceType.*;


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

		if (!query.getType().equals(RANGE_LIUSHUI.getCode())
		&& !query.getType().equals(GlobalConstant.FananceType.RANGE_YINGLI.getCode())){
			throw new ClientErrorException("参数错误");
		}

		//找到所有会员
		Page<Member> page = iMemberRepository.findAll(query);
		PageResp resp = new PageResp(page);
		List<ReturnPointVo> list = new ArrayList<>();
		List<ReturnPointVo> allAist = new ArrayList<>();
		for (Member item :
				page.getContent()) {
			ReturnPointVo vo = getReturnPointVo(query.getType(), item);
			if (vo.getTime().equals(0l) && vo.getMoney()>0) {
				list.add(vo);
			}
			allAist.add(vo);
		}

		if (query.getFindStatus()){
			resp.setData(allAist);
		}else {
			resp.setData(list);
		}
		return HttpResult.success(resp,"获取待结算列表成功!");
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
	HttpResult settle(@PathVariable Long memberId, @RequestBody @Validated ReturnSettle req, BindingResult bindingResult) throws ClientErrorException{
		this.paramsValid(bindingResult);
		GlobalConstant.FananceType type = null;
		if (req.getType().equals(RANGE_LIUSHUI.getCode())) {
			type = RANGE_LIUSHUI;
		}
		if (req.getType().equals(RANGE_YINGLI.getCode())){
			type = RANGE_YINGLI;
		}

		Long time = getReturnTime(memberId,req.getType());
		if (!time.equals(0l)){
			throw new ClientErrorException("据下次结算"+type.getMsg()+"还有"+DateUtil.formatDateTime(time));
		}

		Member member = isNotNull(iMemberRepository.findById(memberId),"无此用户");

		ReturnPointVo vo = getReturnPointVo(req.getType(), member);
		Finance finance = iFinanceService.add(member,vo.getReturnMoney(),iFinanceService.reckonBalance(memberId), type);

		finance.setModifyUser(getLoginUser());
		finance.setModifyTime(System.currentTimeMillis());
		iFinanceRepository.save(finance);


		return HttpResult.success(new FinanceVo(finance),"结算成功");
	}


	/**
	 * 用户的返点Vo
	 */
	private ReturnPointVo getReturnPointVo(Integer type, Member item) throws ClientErrorException {

		ReturnPointVo vo = new ReturnPointVo();
		double returnPoint = 0;

		double money = 0;

		List<Finance> finances = null;
		//上次结算财务记录
		Finance last = iFinanceRepository.findNew(type,item.getId());
		if (null != last){
			finances = iFinanceRepository.findByCreateUserAndCreateTimeAfter(item,last.getCreateTime());
		}else {
			finances = iFinanceRepository.findByCreateUser(item);
		}
		if (null != finances && 0 != finances.size()){
			for (Finance finance :
					finances) {
				if (finance.getType().equals(BET.getCode())) {
					money = Arith.add(money,finance.getMoney());
				}
				if (type.equals(RANGE_YINGLI.getCode())) {
					//分红
					if (finance.getType().equals(BET_WIN.getCode())){
						money = Arith.sub(money,finance.getMoney());
					}
				}
				if (finance.getType().equals(CANCLE.getCode())){
					money = Arith.sub(money,finance.getMoney());
				}
			}
		}

		//找到盘口
		Handicap handicap = item.getHandicap();
		Set<Proportion> proportions = handicap.getProportions();
		for (Proportion proportion :
				proportions) {
			if (type.equals(RANGE_LIUSHUI.getCode())) {
				if (proportion.getReturnPoint().getId().equals(CommonsUtil.RANGE_LIUSHUI_RETURN_POINT)) {
					String[] range = proportion.getRanges().split("-");
					if (money>Double.parseDouble(range[0]) && money<Double.parseDouble(range[1])) {
						returnPoint = Arith.div(proportion.getProportionVal(),100,2);
					}
				}
			}else{
				if (proportion.getReturnPoint().getId().equals(CommonsUtil.RANGE_YINGLI_RETURN_POINT)) {
					String[] range = proportion.getRanges().split("-");
					if (money>Double.parseDouble(range[0]) && money<Double.parseDouble(range[1])) {
						returnPoint = Arith.div(proportion.getProportionVal(),100,2);
					}
				}
			}

		}

		vo.setMoney(money);
		vo.setUsername(item.getUsername());
		vo.setNikename(item.getNickname());
		vo.setReturnMoney(Arith.mul(money,returnPoint));
		vo.setMemberId(item.getId());
		vo.setTime(getReturnTime(item.getId(),type));
		return vo;
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
		}else {
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
}
