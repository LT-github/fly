package com.lt.fly.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.lt.fly.entity.Finance;
import com.lt.fly.entity.User;
import com.lt.fly.jpa.BaseRepository;
import com.lt.fly.jpa.support.QueryBetween;

@Repository
public interface IFinanceRepository extends BaseRepository<Finance, Long>{

	//查用户余额
   List<Finance> findAllByCreateUser(User user);
   //查找用户流水，某个时间段
   List<Finance> findByCreateTimeBetweenAndCreateUserAndType(Long before,Long after,User user,Integer type);
}
