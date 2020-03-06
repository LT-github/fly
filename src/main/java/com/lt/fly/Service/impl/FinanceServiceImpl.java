package com.lt.fly.Service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import com.lt.fly.dao.IMemberRepository;
import com.lt.fly.entity.Member;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.web.query.FinanceFind;
import com.lt.fly.web.req.*;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.FinanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lt.fly.Service.BaseService;
import com.lt.fly.Service.IFinanceService;
import com.lt.fly.dao.IFinanceRepository;
import com.lt.fly.dao.IOrderRepository;
import com.lt.fly.dao.IUserRepository;
import com.lt.fly.entity.Finance;
import com.lt.fly.entity.User;
import com.lt.fly.exception.ClientErrorException;

import static com.lt.fly.utils.GlobalConstant.FananceType.*;

@Service
public class FinanceServiceImpl extends BaseService implements IFinanceService {

	
	@Autowired
	private IUserRepository iUserRepository;
	@Autowired
	private IFinanceRepository iFinanceRepository;

	@Autowired
	private IMemberRepository iMemberRepository;

	@Autowired
	private IOrderRepository iOrderRepository;


	
	@Override
	public Finance add(FinanceAdd req, int type) throws ClientErrorException{
		Finance finance = new Finance();
		finance.setId(idWorker.nextId());
		finance.setCreateTime(System.currentTimeMillis());
		finance.setCreateUser(getLoginUser());
		finance.setMoney(req.getMoney());
		finance.setDescription(req.getDescription());
		finance.setType(type);
		if (finance.getType().equals(BET.getCode()) || finance.getType().equals(DESCEND.getCode()))
			finance.setCountType(GlobalConstant.CountType.SUBTRACT.getCode());
		else
			finance.setCountType(GlobalConstant.CountType.ADD.getCode());
		return finance;
		
	}

	@Override
	public Double reckonBalance(Long userId) throws ClientErrorException {
		Member member = isNotNull(iMemberRepository.findById(userId),"会员不存在");
		BigDecimal sum = new BigDecimal(0);
		for(Finance item:member.getFinances()){

			if(item.getAuditStatus().equals(GlobalConstant.AuditStatus.AUDIT_PASS.getCode())){

				if(item.getCountType().equals(GlobalConstant.CountType.ADD.getCode()))
					sum = sum.add(new BigDecimal(item.getMoney()));
				else
					sum = sum.subtract(new BigDecimal(item.getMoney()));
			}
		}
		return sum.doubleValue();
	}
	
	/**
	 * 计算某个用户,某个时间点余额
	 */
	@Override
	public Double reckonBalanceByTime(FindBlanceBytime req) throws ClientErrorException {
		Optional<User> op = iUserRepository.findById(req.getMemberId());
		if(!op.isPresent())
			throw new ClientErrorException("该用户不存在");
		Double balance = iFinanceRepository.findMemberBlanceByTime(req.getMemberId(), req.getAfter());
		return balance;
	}

	@Override
	public void judgeAuditFinance(JudgeAuditFinanceReq req) throws ClientErrorException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 查询某个会员，某个时间段的流水
	 */
	@Override
	public Double findLiushuiMemberByTime(FindLiushuiReq req) throws ClientErrorException {
		
		Optional<User> op = iUserRepository.findById(req.getMemberId());
		if(!op.isPresent())
			throw new ClientErrorException("该用户不存在");
		Double liushui = iOrderRepository.findLiushuiByCreateTime(req.getBefore(), req.getAfter(), req.getMemberId());
		
		return liushui;
	}
	/**
	 * 查询某个会员，某个时间段的盈亏
	 */
	@Override
	public Double findYingkuiMemberByTime(FindLiushuiReq req) throws ClientErrorException {
		Optional<User> op = iUserRepository.findById(req.getMemberId());
		if(!op.isPresent())
			throw new ClientErrorException("该用户不存在");
		Double yingkui = iOrderRepository.findYingkuiByCreateTime(req.getBefore(), req.getAfter(), req.getMemberId());
		
		return yingkui;
	}

	@Override
	public PageResp<FinanceVo, Finance> findAll(FinanceFind query) {
		Page<Finance> page = iFinanceRepository.findAll(query);
		PageResp<FinanceVo, Finance> resp = new PageResp<>(page);
		resp.setData(FinanceVo.tovo(page.getContent()));
		return resp;
	}


}
