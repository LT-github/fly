package com.lt.fly.dao;


import com.lt.fly.entity.Finance;
import com.lt.fly.entity.User;
import com.lt.fly.jpa.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IFinanceRepository extends BaseRepository<Finance, Long> {

    //查今日财务记录
	List<Finance> findByCreateTimeBetweenAndCreateUser(Long start,Long end,User user);

	//找到最新的财务
    @Query(nativeQuery = true,value = "select * from t_finance f where f.type = :type and create_user_id = :memberId order by create_time desc limit 0,1;")
    Finance findNew(Integer type,Long memberId);

    //查找会员大于某个时间的财务
    List<Finance> findByCreateUserAndCreateTimeAfter(User user,Long createTime);

    //查会员的财务
    List<Finance> findByCreateUser(User user);


    List<Finance> findByCreateUserAndCreateTimeGreaterThanEqualAndCreateTimeLessThan(User user,Long start,Long end);
    
    List<Finance> findByCreateUserAndTypeAndCreateTimeGreaterThanEqualAndCreateTimeLessThan(User user,Integer type,Long start,Long end);
    
    List<Finance> findByCreateUserAndCreateTimeBefore(User user,Long createTime);
    
    List<Finance> findByTypeOrderByCreateTimeDesc(Integer type);
}
