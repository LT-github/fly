package com.lt.fly.Service;


import com.lt.fly.entity.Finance;
import com.lt.fly.entity.Member;
import com.lt.fly.entity.Order;
import com.lt.fly.entity.User;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.web.query.FinanceFind;
import com.lt.fly.web.req.*;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.FinanceVo;


public interface IFinanceService {

	/**
	 * 根据类型添加财务记录
	 */
	
	Finance add(User user, Double money,Double balance, GlobalConstant.FananceType type) throws ClientErrorException;

	/**
	 * 计算某个用户余额
	 */
	Double reckonBalance(Long userId) throws ClientErrorException;
	
	/**
	 * 计算某个用户,某个时间点余额
	 */
	Double reckonBalanceByTime(FindBlanceBytime req) throws ClientErrorException;
	
	/**
	 * 审核会员充值订单
	 */
	
	void judgeAuditFinance(JudgeAuditFinanceReq req) throws ClientErrorException;
	
	/**
	 * 查询某个会员，某个时间段的流水
	 */
	Double findLiushuiMemberByTime(FindLiushuiReq req) throws ClientErrorException;
	
	/**
	 * 查询某个会员，某个时间段的盈亏
	 */
	Double findYingkuiMemberByTime(FindLiushuiReq req) throws ClientErrorException;

	PageResp<FinanceVo,Finance> findAll(FinanceFind query);


	double moneyForReturn(Long start, Long end, Member member,Integer type) throws ClientErrorException;

	Finance findNew(Integer type,Long memberId);
}
