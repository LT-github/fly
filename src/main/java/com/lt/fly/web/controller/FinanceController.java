package com.lt.fly.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lt.fly.Service.IFinanceService;
import com.lt.fly.dao.IFinanceRepository;
import com.lt.fly.dao.IUserRepository;
import com.lt.fly.entity.Finance;
import com.lt.fly.entity.User;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.jpa.support.QueryBetween;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.req.AuditFinanceQ;
import com.lt.fly.web.req.AuditFinanceReq;
import com.lt.fly.web.req.FinanceAddReq;
import com.lt.fly.web.req.FindLiushuiReq;
import com.lt.fly.web.req.JudgeAuditFinanceReq;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.AuditFindVo;


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
	private IFinanceService financeService;
	
	/**
	 * 添加注单订单财务
	 */	
	@PostMapping("/addFinance")
	public HttpResult<Object> addOrderFinance(@RequestBody @Validated FinanceAddReq req) throws ClientErrorException{
		
		Finance finance = new Finance();
		BeanUtils.copyProperties(req, finance);
		finance.setStatus(0);
		finance.setCreateTime(System.currentTimeMillis());
		finance.setModifyTime(System.currentTimeMillis());
		finance.setModifyUser(this.getLoginUser());
		finance.setId(idWorker.nextId());
		finance.setAuditType(1);
		if(finance.getMoney()!=null)
			finance.setMoney(req.getBetsOrder().getTotalMoney());
		Double balance = financeService.reckonBalance(req.getMemberId());
		//判断余额是否够、
		if(balance==null || finance.getMoney()>balance)
			throw new ClientErrorException("用户余额不足");
		Optional<User> op = userRepository.findById(req.getMemberId());
		if(!op.isPresent())
			throw new ClientErrorException("该用户不存在");		
		finance.setCreateUser(op.get());
				
		if(req.getType()!=3)
			throw new ClientErrorException("订单类型异常");
					
		finance.setCountType(2);
		financeRepository.save(finance);
		return HttpResult.success(null,"生成注单财务订单成功！");
		
	}
	
	/**
	 * 查询某个用户的余额
	 */
	@PostMapping("/lookupUserBalance")
	public HttpResult<Object> reckonBalance(Long userId) throws ClientErrorException {

		Double balance = financeService.reckonBalance(userId);


		return HttpResult.success(balance,"余额查询成功");
	}
	

	/**
	 * 客户端请求，生成用户充值订单
	 */	
	@PostMapping("/addAuditMember")
	public HttpResult<Object> addAuditMember(@RequestBody @Validated AuditFinanceReq req) throws ClientErrorException {
		
		Finance finance = new Finance();
		BeanUtils.copyProperties(req, finance);
		finance.setStatus(0);
		finance.setCreateTime(System.currentTimeMillis());
		finance.setModifyTime(System.currentTimeMillis());
		finance.setModifyUser(this.getLoginUser());
		finance.setId(idWorker.nextId());			
		finance.setCreateUser(this.getLoginUser());
		finance.setAuditType(1);
		if(req.getType()!=1)
			throw new ClientErrorException("订单类型异常");
		finance.setCountType(1);		
		financeRepository.save(finance);
		return HttpResult.success(null,"生成充值财务订单成功！");
	}
	/**
	 * 审核充值订单
	 * @param req
	 * @throws ClientErrorException
	 */
	@PostMapping("/judgeAuditFinance")
	public HttpResult<Object> judgeAuditFinance(@RequestBody @Validated JudgeAuditFinanceReq req) throws ClientErrorException {
	
		Optional<Finance> op = financeRepository.findById(req.getAuditFinanceId());
		if(!op.isPresent())
			throw new ClientErrorException("该订单不存在");
		Finance finance = op.get();
		BeanUtils.copyProperties(req, finance);
		
		finance.setModifyTime(System.currentTimeMillis());
		finance.setModifyUser(getLoginUser());
		financeRepository.save(finance);
		return HttpResult.success(null,"修改充值订单状态成功！");
	}
	/**
	 * 根据条件分页查询所有充值或其他订单
	 * @return
	 * @throws ClientErrorException
	 */
	@PostMapping("/findAllAuditFinance")
	public HttpResult<PageResp<AuditFindVo,Finance>> findAllAuditFinance(@RequestBody AuditFinanceQ req) throws ClientErrorException{
		
		Page<Finance> page = financeRepository.findAll(req);
		
		if(page.getContent()==null || page.getContent().isEmpty())
			throw new ClientErrorException("暂时无数据");
		PageResp<AuditFindVo,Finance> resp = new PageResp<AuditFindVo,Finance>(page);
		resp.setData(AuditFindVo.toVo(page.getContent()));
		
		return HttpResult.success(resp, "查询成功");
		
		
	}
	@PostMapping("/findLiushuiMemberByTime")
	public HttpResult<Double> findLiushuiMemberByTime(@RequestBody FindLiushuiReq req) throws ClientErrorException{
		
		Double liushui = financeService.findLiushuiMemberByTime(req);
		
		return HttpResult.success(liushui,"查询流水成功");
	}
}
