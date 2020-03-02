package com.lt.fly.Service;


import com.lt.fly.entity.Finance;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.web.req.AuditFinanceReq;
import com.lt.fly.web.req.FinanceAddReq;
import com.lt.fly.web.req.FindLiushuiReq;
import com.lt.fly.web.req.JudgeAuditFinanceReq;


public interface IFinanceService {

	/**
	 * 添加财务注
	 * 订单
	 */
	
	void addOrderFinance(FinanceAddReq req) throws ClientErrorException;
	
	/**
	 * 计算某个用户余额
	 */
	
	Double reckonBalance(Long userId) throws ClientErrorException;
	
	/**
	 * 生成用户充值订单
	 */
	Finance addAuditMember(AuditFinanceReq req) throws ClientErrorException;
	
	/**
	 * 审核会员充值订单
	 */
	
	void judgeAuditFinance(JudgeAuditFinanceReq req) throws ClientErrorException;
	
	/**
	 * 生成某个会员返点财务
	 */
	void addMemberReturnFinance() throws ClientErrorException;
	
	/**
	 * 查询某个会员，某个时间段的流水
	 */
	Double findLiushuiMemberByTime(FindLiushuiReq req) throws ClientErrorException;
	
}
