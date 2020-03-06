package com.lt.fly.web.controller;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.utils.MyBeanUtils;
import com.lt.fly.web.query.FinanceFind;
import com.lt.fly.web.vo.FinanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.lt.fly.Service.IFinanceService;
import com.lt.fly.dao.IFinanceRepository;
import com.lt.fly.dao.IUserRepository;
import com.lt.fly.entity.Finance;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.req.JudgeAuditFinanceReq;


@RestController
@RequestMapping("/finance")
public class FinanceController extends BaseController{

	
	@Autowired
	private IFinanceRepository financeRepository;	
	@Autowired
    private IdWorker idWorker;
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IFinanceService iFinanceService;
	

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
		Finance finance = isNotNull(financeRepository.findById(req.getAuditFinanceId()),"传递的参数不存在实体");
		MyBeanUtils.copyProperties(req,finance);
		finance.setModifyTime(System.currentTimeMillis());
		finance.setModifyUser(getLoginUser());
		financeRepository.save(finance);
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
}
