package com.lt.fly.Service.impl;

import com.google.common.collect.Lists;
import com.lt.fly.Service.BaseService;
import com.lt.fly.Service.IFinanceService;
import com.lt.fly.dao.*;
import com.lt.fly.entity.*;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.Arith;
import com.lt.fly.utils.CommonsUtil;
import com.lt.fly.utils.DateUtil;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.web.query.FinanceFind;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.FinanceVo;
import com.lt.fly.web.vo.ReturnPointVoByTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.lt.fly.utils.GlobalConstant.FinanceType.*;


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
	
	@Autowired
	private IHandicapRepository handicapRepository;


	
	@Override
	public Finance add(User user, Double money,Double balance, GlobalConstant.FinanceType type) throws ClientErrorException{
		Finance finance = new Finance();
		finance.setId(idWorker.nextId());
		finance.setCreateTime(System.currentTimeMillis());
		finance.setType(type.getCode());
		finance.setMoney(money);
		finance.setDescription(user.getUsername()+type.getMsg()+money+"。");
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

	@Override
	public List<Finance> addTime(Integer settlementType, Long settleStartTime, Long settleEndTime,
			List<Long> handicapIds,Integer types) throws ClientErrorException{
		List<Handicap> handicaps=Lists.newArrayList();
		List<Finance> fi=new ArrayList<>();
		GlobalConstant.FinanceType type = GlobalConstant.FinanceType.getFinanceTypeByCode(settlementType);

		if(null==handicapIds) { handicaps = handicapRepository.findAllBySettlementType(types);} else {handicaps = handicapRepository.findAllByIdAndSettlementType(handicapIds, types);}
		if(handicaps== null || handicaps.size()==0) throw new ClientErrorException("暂时无任何盘口");		
		for (Handicap handicap : handicaps) {					
			Set<Member> members = handicap.getMembers();
			List<Member> memberss=new ArrayList<>(members);
			
			if(members==null || members.size()==0) continue;
			for (Member member : memberss) {
				//上次结算财务记录
				Finance last = iFinanceRepository.findNew(settlementType,member.getId());
								
				List<Finance> f = iFinanceRepository.findByCreateUserAndTypeAndCreateTimeGreaterThanEqualAndCreateTimeLessThan(member, settlementType, settleStartTime, settleEndTime);
				  if(f!=null) continue;
				if(handicap.getSettlementType()==1) {
					
					if(last!=null) {
					settleStartTime=last.getCreateTime();
					}else {
						settleStartTime=0l;
					}
					settleEndTime=handicap.getSettlementTime();							
				}
				
				ReturnPointVoByTime vo = getReturnPointVoByTime(settlementType, member,settleStartTime,settleEndTime);
				if(vo.getReturnMoney()==0) continue;
				Finance finance = add(member,vo.getReturnMoney(),reckonBalance(member.getId()), type);			
				finance.setModifyUser(getLoginUser());
				finance.setModifyTime(System.currentTimeMillis());
				finance.setDescription(member.getNickname()+type.getMsg()+vo.getReturnMoney()+"。"+vo.getTime());
				iFinanceRepository.save(finance);				
				fi.add(finance);
				 
			}
		}
		return fi;
	}
	/*
	普通会员按时间的返点金额
	 */
	private double getMoneyByTime(Integer type, Member member, Finance last,Long settleStartTime,Long settleEndTime) throws ClientErrorException {
		double money = 0;
		List<Finance> finances;	
				
		if(last==null) {		         
		        finances = iFinanceRepository.findByCreateUser(member);
		}else {
			 
			finances = iFinanceRepository.findByCreateUserAndCreateTimeGreaterThanEqualAndCreateTimeLessThan(member,settleStartTime,settleEndTime);
			
		}
		if (null != finances && 0 != finances.size()){
			money =  Arith.sub(getReduce(new HashSet<>(finances), BET_RESULT),
					Arith.sub(getReduce(new HashSet<>(finances), BET),getReduce(new HashSet<>(finances), BET_CANCLE)));
			if (money < 0) {
				return Arith.round(Math.abs(money),2);
			}
		}


		return 0;
	}

	/*
	推手会员按时间的返点金额
	 */
	private double getAllMoneyByTime(Integer type, Member member, Finance last,Long settleStartTime,Long settleEndTime) throws ClientErrorException{
		double money = 0;
		List<Member> members = iMemberRepository.findByModifyUser(member);
		if(members!= null && members.size()!=0) {
		for (Member item :
				members) {
			money = Arith.add(money,getMoneyByTime(type,item,last,settleStartTime,settleEndTime));
		}
		  }
		return money;
	}
	public ReturnPointVoByTime getReturnPointVoByTime(Integer type, Member member,Long settleStartTime,Long settleEndTime) throws ClientErrorException{

		ReturnPointVoByTime vo = new ReturnPointVoByTime();
		double returnPoint = 0;
		double money = 0;
		//上次结算财务记录
		Finance last = iFinanceRepository.findNew(type,member.getId());
		

		//找到返点值
		Handicap handicap = member.getHandicap();
		Set<Proportion> proportions = null;
		//普通会员与推手会员的返点
		if (member.getType()==2) {
			proportions = member.getProportions();
			money = getAllMoneyByTime(type,member,last,settleStartTime,settleEndTime);
		} else {
			money = getMoneyByTime(type, member, last,settleStartTime,settleEndTime);
			if(handicap!=null)
				proportions = handicap.getProportions();
		}
		if(proportions!=null) {
			for (Proportion proportion :
					proportions) {									
					returnPoint = getReturnPoint(money, proportion, member.getType()==2?CommonsUtil.REFERRAL_LIUSHUI_RETURN_POINT:CommonsUtil.RANGE_LIUSHUI_RETURN_POINT);				
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
		vo.setTime(DateUtil.stampToDate(settleStartTime)+" 至 "+DateUtil.stampToDate(settleEndTime));
		return vo;


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
}
