package com.lt.fly.Service.impl;

import java.math.BigDecimal;
import java.util.Set;

import com.lt.fly.dao.IMemberRepository;
import com.lt.fly.entity.Member;
import com.lt.fly.utils.Arith;
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

import static com.lt.fly.utils.GlobalConstant.FinanceType.DESCEND;
import static com.lt.fly.utils.GlobalConstant.FinanceType.RECHARGE;


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
	public Finance add(User user, Double money,Double balance, GlobalConstant.FinanceType type) throws ClientErrorException{
		Finance finance = new Finance();
		finance.setId(idWorker.nextId());
		finance.setCreateTime(System.currentTimeMillis());
		finance.setType(type.getCode());
		finance.setMoney(money);
		if(user.getNickname()!=null)
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
			//下注//撤销流水
			case BET:case TIMELY_LISHUI_CANCLE:
				finance.setCountType(GlobalConstant.CountType.SUBTRACT.getCode());
				break;
			//撤销//返点
			case BET_CANCLE:case BET_RESULT:case TIMELY_LIUSHUI:case RANGE_LIUSHUI:case RANGE_YINGLI:
			case REFERRAL_YINGLI:case REFERRAL_LIUSHUI:case SETTLEMENT_TYPE_HAND:
				finance.setCountType(GlobalConstant.CountType.ADD.getCode());
				break;
			//系统上分
			case SYSTEM_RECHARGE:
				finance.setCountType(GlobalConstant.CountType.ADD.getCode());
				finance.setModifyUser(getLoginUser());
				break;
			//系统下分
			case SYSTEM_DESCEND:
				finance.setCountType(GlobalConstant.CountType.SUBTRACT.getCode());
				finance.setModifyUser(getLoginUser());
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
		double balance = 0;
		if (null == member.getFinances() || member.getFinances().isEmpty()){
			return balance;
		}
		for(Finance item:member.getFinances()){
			if(item.getMoney()==null || item==null) continue;
			if(item.getCountType()==null) continue;
			if(item.getCountType().equals(GlobalConstant.CountType.ADD.getCode())){
				balance = Arith.add(balance,item.getMoney());
				if (null != item.getAuditStatus() && !item.getAuditStatus().equals(GlobalConstant.AuditStatus.AUDIT_PASS.getCode())) {
					balance = Arith.sub(balance,item.getMoney());
				}
			} else {
				balance = Arith.sub(balance,item.getMoney());
				if (null != item.getAuditStatus() && !item.getAuditStatus().equals(GlobalConstant.AuditStatus.AUDIT_PASS.getCode())) {
					balance = Arith.add(balance,item.getMoney());
				}
			}
		}
		return balance;
	}
	
	@Override
	public PageResp<FinanceVo, Finance> findAll(FinanceFind query) {
		Page<Finance> page = iFinanceRepository.findAll(query);
		PageResp<FinanceVo, Finance> resp = new PageResp<>(page);
		resp.setData(FinanceVo.tovo(page.getContent()));
		return resp;
	}

	@Override
	public Double getReduce(Set<Finance> finances, GlobalConstant.FinanceType financeType) {
		if (financeType.equals(RECHARGE) || financeType.equals(DESCEND)){
			return finances.stream().filter(finance -> finance.getType().equals(financeType.getCode())
					&& finance.getAuditStatus().equals(GlobalConstant.AuditStatus.AUDIT_PASS.getCode()))
					.map(Finance::getMoney)
					.reduce(0.0, (a, b) -> Arith.add(a, b));
		}
		return finances.stream().filter(finance -> finance.getType().equals(financeType.getCode()))
				.map(Finance::getMoney)
				.reduce(0.0, (a, b) -> Arith.add(a, b));
	}
}
