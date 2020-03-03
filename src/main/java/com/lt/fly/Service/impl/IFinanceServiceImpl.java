package com.lt.fly.Service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.fly.Service.BaseService;
import com.lt.fly.Service.IFinanceService;
import com.lt.fly.dao.IFinanceRepository;
import com.lt.fly.dao.IOrderRepository;
import com.lt.fly.dao.IUserRepository;
import com.lt.fly.entity.Finance;
import com.lt.fly.entity.Member;
import com.lt.fly.entity.User;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.jpa.support.QueryBetween;
import com.lt.fly.web.req.AuditFinanceReq;
import com.lt.fly.web.req.FinanceAddReq;
import com.lt.fly.web.req.FindBlanceBytime;
import com.lt.fly.web.req.FindLiushuiReq;
import com.lt.fly.web.req.JudgeAuditFinanceReq;

@Service
public class IFinanceServiceImpl extends BaseService implements IFinanceService {

	
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IFinanceRepository financeRepository;
	@Autowired
	IFinanceService financeService;
	@Autowired
	IOrderRepository orderRepository;
	
	/**
	 * 添加注单订单财务
	 */
	@Override
	public void addOrderFinance(FinanceAddReq req) throws ClientErrorException{

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
		
	}
	/**
	 * 添加撤销财务订单
	 */
	@Override
	public void addRevokeOrderFinance(FinanceAddReq req) throws ClientErrorException {
		Finance finance = new Finance();
		BeanUtils.copyProperties(req, finance);
		finance.setStatus(0);
		finance.setCreateTime(System.currentTimeMillis());
		finance.setModifyTime(System.currentTimeMillis());
		finance.setModifyUser(this.getLoginUser());
		finance.setId(idWorker.nextId());
		//finance.setAuditType(1);
		if(finance.getMoney()!=null)
			finance.setMoney(req.getBetsOrder().getTotalMoney());
		Double balance = financeService.reckonBalance(req.getMemberId());
		//判断余额
		if(balance==null)
			balance=0.0;
		Optional<User> op = userRepository.findById(req.getMemberId());
		if(!op.isPresent())
			throw new ClientErrorException("该用户不存在");		
		finance.setCreateUser(op.get());
				
		if(req.getType()!=6)
			throw new ClientErrorException("订单类型异常");
					
		finance.setCountType(1);
		financeRepository.save(finance);
		
	}
	/**
	 * 计算某个用户的余额
	 */
	@Override
	public Double reckonBalance(Long userId) throws ClientErrorException {
		

		Optional<User> op = userRepository.findById(userId);
		if(!op.isPresent())
			throw new ClientErrorException("该用户不存在");
		Double balance = financeRepository.findMemberBlance(userId);				
		return balance;
	}
	
	/**
	 * 计算某个用户,某个时间点余额
	 */
	@Override
	public Double reckonBalanceByTime(FindBlanceBytime req) throws ClientErrorException {
		Optional<User> op = userRepository.findById(req.getMemberId());
		if(!op.isPresent())
			throw new ClientErrorException("该用户不存在");
		Double balance = financeRepository.findMemberBlanceByTime(req.getMemberId(), req.getAfter());
		return balance;
	}
	/**
	 * 生成用户充值订单
	 */
	@Override
	public Finance addAuditMember(AuditFinanceReq req) throws ClientErrorException {
		
								
		return null;
	}
	@Override
	public void judgeAuditFinance(JudgeAuditFinanceReq req) throws ClientErrorException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addMemberReturnFinance() throws ClientErrorException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 查询某个会员，某个时间段的流水
	 */
	@Override
	public Double findLiushuiMemberByTime(FindLiushuiReq req) throws ClientErrorException {
		
		Optional<User> op = userRepository.findById(req.getMemberId());
		if(!op.isPresent())
			throw new ClientErrorException("该用户不存在");
		Double liushui = orderRepository.findLiushuiByCreateTime(req.getBefore(), req.getAfter(), req.getMemberId());
		
		return liushui;
	}
	/**
	 * 查询某个会员，某个时间段的盈亏
	 */
	@Override
	public Double findYingkuiMemberByTime(FindLiushuiReq req) throws ClientErrorException {
		Optional<User> op = userRepository.findById(req.getMemberId());
		if(!op.isPresent())
			throw new ClientErrorException("该用户不存在");
		Double yingkui = orderRepository.findYingkuiByCreateTime(req.getBefore(), req.getAfter(), req.getMemberId());
		
		return yingkui;
	}
	
	

}
