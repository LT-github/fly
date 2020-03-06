package com.lt.fly.dao;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.utils.GlobalConstant;
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

	@Query(nativeQuery = true,value = "select t1.*,t2.AuditFinance,t2.BetsFinance,t2.ReturnPointFinance " +
			"from (select from_unixtime(create_time / 1000, '%Y-%m-%d') dat, " +
			"count(*) betcount," +
			"sum(ifnull(total_money,0)) water," +
			"sum(ifnull(battle_result,0)) betResult " +
			"from t_bets_order group by from_unixtime(create_time / 1000, '%Y-%m-%d')) as t1 " +
			"left join (select from_unixtime(create_time / 1000, '%Y-%m-%d') dat," +
			"sum(case when t.type=1 then t.money end) as recharge," +
			"sum(case when t.type=7  then t.money end) as descend," +
			"sum(case when t.type=6  then t.money end) as yingli," +
			"sum(case when t.type=6  then t.money end) as yingli " +
			"from t_finance t where t.discriminator in ('AuditFinance','BetsFinance','ReturnPointFinance') " +
			"group by from_unixtime(create_time / 1000, '%Y-%m-%d')) as t2 " +
			"on t1.dat = t2.dat")
	Object[] findReport(DataQueryObjectPage page);
	
}
