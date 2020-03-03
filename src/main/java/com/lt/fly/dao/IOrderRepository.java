package com.lt.fly.dao;

import org.springframework.data.jpa.repository.Query;

import com.lt.fly.entity.Order;
import com.lt.fly.entity.User;
import com.lt.fly.jpa.BaseRepository;

public interface IOrderRepository extends BaseRepository<Order, Long> {
	
	//查询某个会员，某个时间段的盈亏
	@Query(nativeQuery = true,value="SELECT ")
	Double findYingkuiByCreateTime(Long before,Long after,User user,Integer type);
	
	
}
