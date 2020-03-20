package com.lt.fly.Service.impl;

import java.math.BigDecimal;
import com.lt.fly.dao.IMemberRepository;
import com.lt.fly.entity.Member;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.web.query.FinanceFind;
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
	public Finance add(User user, Double money,Double balance, GlobalConstant.FananceType type) throws ClientErrorException{
		Finance finance = new Finance();
		finance.setId(idWorker.nextId());
		finance.setCreateTime(System.currentTimeMillis());
		finance.setType(type.getCode());
		finance.setMoney(money);
		finance.setDescription(user.getNickname()+type.getMsg()+money+"。");
		finance.setCreateUser(user);
		if (null == balance)
			balance = reckonBalance(user.getId());
		finance.setBalance(balance);

		switch (type){
			//上分
			case RECHARGE:
				finance.setCountType(GlobalConstant.CountType.ADD.getCode());
				finance.setAuditStatus(GlobalConstant.AuditStatus.IN_AUDIT.getCode());
				break;
			//下分
			case DESCEND:
				finance.setCountType(GlobalConstant.CountType.SUBTRACT.getCode());
				finance.setAuditStatus(GlobalConstant.AuditStatus.IN_AUDIT.getCode());
				break;
			//下注
			case BET:
				finance.setCountType(GlobalConstant.CountType.SUBTRACT.getCode());
				break;
			//撤销,下注获胜
			case CANCLE:case BET_WIN:
				finance.setCountType(GlobalConstant.CountType.ADD.getCode());
				break;
			//返点
			case TIMELY_LIUSHUI:case RANGE_LIUSHUI:case RANGE_YINGLI:
				finance.setCountType(GlobalConstant.CountType.ADD.getCode());
				break;
			default:
				break;
		}
		iFinanceRepository.save(finance);
		return finance;
	}

	@Override
	public Double reckonBalance(Long userId) throws ClientErrorException {
		Member member = isNotNull(iMemberRepository.findById(userId),"会员不存在");
		BigDecimal sum = new BigDecimal(0);
		for(Finance item:member.getFinances()){
			if(item.getCountType().equals(GlobalConstant.CountType.ADD.getCode())){
				sum = sum.add(new BigDecimal(item.getMoney().toString()));
				if (null != item.getAuditStatus() && !item.getAuditStatus().equals(GlobalConstant.AuditStatus.AUDIT_PASS.getCode())) {
					sum = sum.subtract(new BigDecimal(item.getMoney().toString()));
				}
			} else {
				sum = sum.subtract(new BigDecimal(item.getMoney().toString()));
				if (null != item.getAuditStatus() && !item.getAuditStatus().equals(GlobalConstant.AuditStatus.AUDIT_PASS.getCode())) {
					sum = sum.add(new BigDecimal(item.getMoney().toString()));
				}
			}
		}
		return sum.doubleValue();
	}
	
	@Override
	public PageResp<FinanceVo, Finance> findAll(FinanceFind query) {
		Page<Finance> page = iFinanceRepository.findAll(query);
		PageResp<FinanceVo, Finance> resp = new PageResp<>(page);
		resp.setData(FinanceVo.tovo(page.getContent()));
		return resp;
	}
}
