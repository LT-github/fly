package com.lt.fly.dao;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.web.query.BetReportFind;
import com.lt.fly.web.resp.BetReportResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.lt.fly.entity.Order;
import com.lt.fly.entity.User;
import com.lt.fly.jpa.BaseRepository;

import java.util.List;

public interface IOrderRepository extends BaseRepository<Order, Long> {
	
	//查询某个会员，某个时间段的盈亏
	@Query(nativeQuery = true,value="SELECT (ifnull(a.snum1,0)-ifnull(b.snum2,0)) " + 
			"FROM " + 
			"(SELECT sum(ifnull(battle_result,0)) snum1 from t_order WHERE result_type=1 AND status in(0,1) AND lottery_result=0 AND create_time Between ?1 and ?2 AND create_user_id=?3) a," + 
			"(SELECT sum(ifnull(battle_result,0)) snum2 from t_order WHERE result_type=1 AND status in(0,1) AND lottery_result=1 AND create_time Between ?1 and ?2 AND create_user_id=?3) b ")
	Double findYingkuiByCreateTime(Long before,Long after,Long memberId);
	
	//查询某个会员，某个时间段的流水
	@Query(nativeQuery = true,value="SELECT sum(ifnull(total_money,0)) FROM t_order WHERE status in(0,1) AND create_time Between ?1 and ?2 AND create_user_id=?3")
	Double findLiushuiByCreateTime(Long before,Long after,Long memberId);

	@Query(nativeQuery = true,value = "SELECT * FROM t_order WHERE create_time BETWEEN 1? and 2? AND create_user_id = 3?")
	List<Order> findByUserAndTime(Long before,Long after,Long memberId);

	@Query(nativeQuery = true,value = "select t1.dat,ifnull(t1.betcount,0),ifnull(t1.betResult,0),ifnull(t1.water,0),ifnull(t1.recharge,0),\n" +
			"ifnull(t1.descend,0),ifnull(t1.huishui,0),ifnull(t1.fenghong,0) \n" +
			"from (\n" +
			"select from_unixtime(create_time / 1000, '%Y-%m-%d') as dat,\n" +
			"ifnull(count(if(t_order.status != 2,1,0)),0) as betcount,\n" +
			"ifnull(sum(ifnull(battle_result,0)),0) as betResult ,\n" +
			"null as water,\n" +
			"null as recharge,\n" +
			"null as descend,\n" +
			"null as huishui,\n" +
			"null as fenghong\n" +
			"from t_order \n" +
			"group by from_unixtime(create_time / 1000, '%Y-%m-%d') \n" +
			"union select \n" +
			"from_unixtime(create_time / 1000, '%Y-%m-%d') as dat,\n" +
			"null as betcount,\n" +
			"null as betResult,\n" +
			"sum(if(t.type=2 ,t.money,0)) - sum(if(t.type=3 ,t.money,0))as water,\n" +
			"sum(if(t.type=1 and t.audit_status=1,t.money,0)) as recharge,\n" +
			"sum(if(t.type=7 and t.audit_status=1,t.money,0)) as descend,\n" +
			"ifnull(sum(if(t.type=4,t.money,0)),0) + ifnull(sum(if(t.type=5,t.money,0)),0) as huishui,\n" +
			"sum(if(t.type=6,t.money,0)) as fenghong\n" +
			"from t_finance t \n" +
			"group by from_unixtime(create_time / 1000, '%Y-%m-%d')) as t1 \n" +
			"where t1.dat in (from_unixtime(?1/ 1000, '%Y-%m-%d'),from_unixtime(?2/ 1000, '%Y-%m-%d'))")
	Page<Object[]> findReport(long start,long end,Pageable pageable);
	
}
