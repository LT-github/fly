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

	@Query(nativeQuery = true,value = "select t1.*,t2.water,t2.recharge,t2.descend,t2.huishui,t2.fenghong " +
			"from " +
			"(select " +
			"from_unixtime(create_time / 1000, '%Y-%m-%d') as dat, " +
			"count(*) as betcount," +
			"sum(ifnull(battle_result,0)) as betResult " +
			"from t_bets_order " +
			"group by from_unixtime(create_time / 1000, '%Y-%m-%d')) as t1 " +
			"left join " +
			"(select " +
			"sum(if(t.type=2 ,t.money,0)) - sum(if(t.type=3 ,t.money,0))as water," +
			"sum(if(t.type=1 and t.audit_status=1,t.money,0))) as recharge," +
			"sum(if(t.type=7 and t.audit_status=1,t.money,0))) as descend," +
			"sum(if(t.type=4,t.money,0))) + sum(if(t.type=5,t.money,0))) as huishui," +
			"sum(if(t.type=6,t.money,0))) as fenghong " +
			"from t_finance t " +
			"group by from_unixtime(create_time / 1000, '%Y-%m-%d')) as t2 " +
			"on t1.dat = t2.dat " +
			"where t1.dat in (from_unixtime(:start / 1000, '%Y-%m-%d'),from_unixtime(:end / 1000, '%Y-%m-%d'))")
	Page<Object[]> findReport(long start,long end,Pageable pageable);
	
}
