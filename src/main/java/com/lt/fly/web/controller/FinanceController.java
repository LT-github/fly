package com.lt.fly.web.controller;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IMemberRepository;
import com.lt.fly.dao.IOrderRepository;
import com.lt.fly.entity.Handicap;
import com.lt.fly.entity.Member;
import com.lt.fly.entity.Proportion;
import com.lt.fly.utils.*;
import com.lt.fly.web.query.FinanceFind;
import com.lt.fly.web.query.ReturnPointFind;
import com.lt.fly.web.req.FinanceAdd;
import com.lt.fly.web.req.ReturnSettle;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.resp.ReturnPointVo;
import com.lt.fly.web.vo.FinanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.lt.fly.Service.IFinanceService;
import com.lt.fly.dao.IFinanceRepository;
import com.lt.fly.dao.IUserRepository;
import com.lt.fly.entity.Finance;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.web.req.JudgeAuditFinanceReq;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.lt.fly.utils.GlobalConstant.FananceType.RANGE_LIUSHUI;
import static com.lt.fly.utils.GlobalConstant.FananceType.RANGE_YINGLI;


@RestController
@RequestMapping("/finance")
public class FinanceController extends BaseController{

	
	@Autowired
	private IFinanceRepository iFinanceRepository;
	@Autowired
    private IdWorker idWorker;
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IFinanceService iFinanceService;

	@Autowired
	private IMemberRepository iMemberRepository;

	@Autowired
	private IOrderRepository iOrderRepository;

	

//	/**
////	 * 查询某个用户的余额
////	 */
////	@PostMapping("/lookupUserBalance/{userId}")
////	public HttpResult<Object> reckonBalance(@PathVariable Long userId) throws ClientErrorException {
////
////		Double balance = financeService.reckonBalance(userId);
////
////
////		return HttpResult.success(balance,"余额查询成功");
////	}
////	@PostMapping("/reckonBalanceByTime")
////	public HttpResult<Double> reckonBalanceByTime(@RequestBody @Validated FindBlanceBytime  req) throws ClientErrorException{
////		Double balance = financeService.reckonBalanceByTime(req);
////
////		return  HttpResult.success(balance,"余额查询成功");
////	}

//	/**
//	 * 客户端请求，生成用户充值订单
//	 */
//	@PostMapping("/addAuditMember")
//	public HttpResult<Object> addAuditMember(@RequestBody @Validated AuditFinanceReq req) throws ClientErrorException {
//
////		Finance finance = new Finance();
////		BeanUtils.copyProperties(req, finance);
////		finance.setStatus(0);
////		finance.setCreateTime(System.currentTimeMillis());
////		finance.setModifyTime(System.currentTimeMillis());
////		finance.setModifyUser(this.getLoginUser());
////		finance.setId(idWorker.nextId());
////		finance.setCreateUser(this.getLoginUser());
////		finance.setAuditType(1);
////		if(req.getType()!=1)
////			throw new ClientErrorException("订单类型异常");
////		finance.setCountType(1);
////		financeRepository.save(finance);
////		return HttpResult.success(null,"生成充值财务订单成功！");
//	}
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
//	/**
//	 * 根据条件分页查询所有充值或其他订单
//	 * @return
//	 * @throws ClientErrorException
//	 */
//	@PostMapping("/findAllAuditFinance")
//	public HttpResult<PageResp<AuditFindVo,Finance>> findAllAuditFinance(@RequestBody AuditFinanceQ req) throws ClientErrorException{
//
//		Page<Finance> page = financeRepository.findAll(req);
//
//		if(page.getContent()==null || page.getContent().isEmpty())
//			throw new ClientErrorException("暂时无数据");
//		PageResp<AuditFindVo,Finance> resp = new PageResp<AuditFindVo,Finance>(page);
//		resp.setData(AuditFindVo.toVo(page.getContent()));
//
//		return HttpResult.success(resp, "查询成功");
//
//
//	}

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


//	@PostMapping("/findLiushuiMemberByTime")
//	public HttpResult<Double> findLiushuiMemberByTime(@RequestBody @Validated FindLiushuiReq req) throws ClientErrorException{
//
//		Double liushui = IFinanceService.findLiushuiMemberByTime(req);
//
//		return HttpResult.success(liushui,"查询流水成功");
//	}
//	@PostMapping("/findYingkuiMemberByTime")
//	public HttpResult<Double> findYingkuiMemberByTime(@RequestBody @Validated FindLiushuiReq req) throws ClientErrorException{
//
//		Double yingkui = financeService.findYingkuiMemberByTime(req);
//		return HttpResult.success(yingkui,"查询盈亏成功");
//	}

	/**
	 * 待分红与回水列表
	 * @return
	 * @throws ClientErrorException
	 */
	@GetMapping("return")
	@UserLoginToken
	public HttpResult willReturnFind(ReturnPointFind query) throws ClientErrorException{

		if (!query.getType().equals(RANGE_LIUSHUI.getCode())
		&& !query.getType().equals(GlobalConstant.FananceType.RANGE_YINGLI.getCode())){
			throw new ClientErrorException("参数错误");
		}

		//找到所有会员
		Page<Member> page = iMemberRepository.findAll(query);
		PageResp resp = new PageResp(page);
		List<ReturnPointVo> list = new ArrayList<>();
		for (Member item :
				page.getContent()) {

			ReturnPointVo vo = new ReturnPointVo();
			double returnPoint = 0;

			Double money = null;
			//这次待结算的流水
			Finance finance = iFinanceRepository.findNew(query.getType(),item.getId());
			if (null == finance) {
				money = iOrderRepository.findLiushuiByCreateTime(item.getId());
			}else {
				money = iOrderRepository.findLiushuiByCreateTime(finance.getCreateTime(),System.currentTimeMillis(),item.getId());
			}

			double returnMoney = money==null?0:money;
			//找到盘口
			Handicap handicap = item.getHandicap();
			Set<Proportion> proportions = handicap.getProportions();
			for (Proportion proportion :
					proportions) {
				if (query.getType().equals(RANGE_LIUSHUI.getCode())) {
					if (proportion.getReturnPoint().getId().equals(CommonsUtil.RANGE_LIUSHUI_RETURN_POINT)) {
						String[] range = proportion.getRanges().split("-");
						if (returnMoney>Double.parseDouble(range[0]) && returnMoney<Double.parseDouble(range[1])) {
							returnPoint = Arith.div(proportion.getProportionVal(),100,2);
						}
					}
				}else{
					if (proportion.getReturnPoint().getId().equals(CommonsUtil.RANGE_YINGLI_RETURN_POINT)) {
						String[] range = proportion.getRanges().split("-");
						if (returnMoney>Double.parseDouble(range[0]) && returnMoney<Double.parseDouble(range[1])) {
							returnPoint = Arith.div(proportion.getProportionVal(),100,2);
						}
					}
				}

			}

			vo.setMoney(returnMoney);
			vo.setUsername(item.getUsername());
			vo.setNikename(item.getNickname());
			vo.setReturnMoney(Arith.mul(returnMoney,returnPoint));
			list.add(vo);
		}

		resp.setData(list);
		return HttpResult.success(resp,"获取待结算列表成功!");
	}

	@PutMapping("/return/{memberId}")
	@UserLoginToken
	HttpResult settle(@PathVariable Long memberId,@RequestBody ReturnSettle req) throws ClientErrorException{
		Member member = isNotNull(iMemberRepository.findById(memberId),"无此用户");
		GlobalConstant.FananceType type = null;

		if (req.getType().equals(RANGE_LIUSHUI.getCode())) {
			type = RANGE_LIUSHUI;
		}
		if (req.getType().equals(RANGE_YINGLI.getCode())){
			type = RANGE_YINGLI;
		}

		Finance finance = iFinanceService.add(member,req.getMoney(),null, type);

		finance.setModifyUser(getLoginUser());
		finance.setModifyTime(System.currentTimeMillis());
		iFinanceRepository.save(finance);


		return HttpResult.success(new FinanceVo(finance),"结算成功");
	}
	
}
