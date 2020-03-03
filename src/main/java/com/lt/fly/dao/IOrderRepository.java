package com.lt.fly.dao;

import org.springframework.data.jpa.repository.Query;

import com.lt.fly.entity.Order;
import com.lt.fly.entity.User;
import com.lt.fly.jpa.BaseRepository;

public interface IOrderRepository extends BaseRepository<Order, Long> {
	
	//查询某个会员，某个时间段的盈亏
	@Query(nativeQuery = true,value="SELECT (ifnull(a.snum1,0)-ifnull(b.snum2,0)) " + 
			"FROM " + 
			"(SELECT sum(battle_result) snum1 from t_order WHERE result_type=1 AND status in(0,1) AND lottery_result=0 AND create_time Between ?1 and ?2 AND create_user_id=?3) a," + 
			"(SELECT sum(battle_result) snum2 from t_order WHERE result_type=1 AND status in(0,1) AND lottery_result=1 AND create_time Between ?1 and ?2 AND create_user_id=?3) b ")
	Double findYingkuiByCreateTime(Long before,Long after,Long memberId);
	
	
}
