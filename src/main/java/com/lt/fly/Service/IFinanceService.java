package com.lt.fly.Service;


import com.lt.fly.entity.Finance;
import com.lt.fly.entity.User;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.web.query.FinanceFind;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.FinanceVo;

import java.util.List;
import java.util.Set;


public interface IFinanceService {
	//按照类型添加财务
	Finance add(User user, Double money,Double balance, GlobalConstant.FinanceType type) throws ClientErrorException;
	//查询用户余额
	Double reckonBalance(Long userId) throws ClientErrorException;
	//所有财务信息
	PageResp<FinanceVo,Finance> findAll(FinanceFind query);


	Double getReduce(Set<Finance> finances, GlobalConstant.FinanceType financeType);
	
	List<Finance> addTime(Integer settlementType,Long settleStartTime,Long settleEndTime,List<Long> handicapIds,Integer type)throws ClientErrorException; 
}
