package com.lt.fly.dao;


import java.util.List;

import com.lt.fly.jpa.support.QueryField;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lt.fly.entity.Finance;
import com.lt.fly.entity.User;
import com.lt.fly.jpa.BaseRepository;


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

    @Query(nativeQuery = true,value = "select t1.dat,ifnull(sum(t1.betcount),0),ifnull(sum(t1.water),0),ifnull(sum(t1.betResult),0),ifnull(sum(t1.recharge),0),\n" +
            "ifnull(sum(t1.descend),0),ifnull(sum(t1.huishui),0),ifnull(sum(t1.fenghong),0),ifnull(sum(t1.profit),0)\n" +
            "from (select \n" +
            "from_unixtime(create_time / 1000, '%Y-%m-%d') as dat,\n" +
            "count(if(t.type = 2, 1, null)) - count(if(t.type = 3, 1, null)) as betcount,\n" +
            "sum(if(t.type=2 ,t.money,0)) - sum(if(t.type=3 ,t.money,0))as water,\n" +
            "sum(if(t.type=8,t.money,0)) as betResult,\n" +
            "sum(if(t.type=1 and t.audit_status=1,t.money,0)) as recharge,\n" +
            "sum(if(t.type=7 and t.audit_status=1,t.money,0)) as descend,\n" +
            "sum(if(t.type=2 ,t.money,0)) - sum(if(t.type=3 ,t.money,0)) - sum(if(t.type=8,t.money,0)) as profit,\n" +
            "ifnull(sum(if(t.type=4,t.money,0)),0) + ifnull(sum(if(t.type=5,t.money,0)),0) as huishui,\n" +
            "sum(if(t.type=6,t.money,0)) as fenghong\n" +
            "from t_finance t \n" +
            "group by from_unixtime(create_time / 1000, '%Y-%m-%d')) as t1 \n" +
            "where t1.dat between from_unixtime(:start/ 1000, '%Y-%m-%d') and from_unixtime(:end/ 1000, '%Y-%m-%d')\n" +
            "group by t1.dat\n" +
            "order by t1.dat desc\n" +
            "limit :page,:size")
    List<Object[]> findReport(long start,long end,long page,int size);


    @Query(nativeQuery = true,value = "select count(*) \n" +
            "from(select t1.dat,ifnull(sum(t1.betcount),0),ifnull(sum(t1.water),0),ifnull(sum(t1.betResult),0),ifnull(sum(t1.recharge),0),\n" +
            "ifnull(sum(t1.descend),0),ifnull(sum(t1.huishui),0),ifnull(sum(t1.fenghong),0),ifnull(sum(t1.profit),0)\n" +
            "from (select \n" +
            "from_unixtime(create_time / 1000, '%Y-%m-%d') as dat,\n" +
            "count(if(t.type = 2, 1, null)) - count(if(t.type = 3, 1, null)) as betcount,\n" +
            "sum(if(t.type=2 ,t.money,0)) - sum(if(t.type=3 ,t.money,0))as water,\n" +
            "sum(if(t.type=8,t.money,0)) as betResult,\n" +
            "sum(if(t.type=1 and t.audit_status=1,t.money,0)) as recharge,\n" +
            "sum(if(t.type=7 and t.audit_status=1,t.money,0)) as descend,\n" +
            "sum(if(t.type=2 ,t.money,0)) - sum(if(t.type=3 ,t.money,0)) - sum(if(t.type=8,t.money,0)) as profit,\n" +
            "ifnull(sum(if(t.type=4,t.money,0)),0) + ifnull(sum(if(t.type=5,t.money,0)),0) as huishui,\n" +
            "sum(if(t.type=6,t.money,0)) as fenghong\n" +
            "from t_finance t \n" +
            "group by from_unixtime(create_time / 1000, '%Y-%m-%d')) as t1 \n" +
            "where t1.dat between from_unixtime(:start/ 1000, '%Y-%m-%d') and from_unixtime(:end/ 1000, '%Y-%m-%d')\n" +
            "group by t1.dat\n" +
            "order by t1.dat ) as t3")
    Long countByReport(long start,long end);

    List<Finance> findByCreateUserAndCreateTimeGreaterThanEqualAndCreateTimeLessThan(User user,Long start,Long end);
    List<Finance> findByCreateUserAndCreateTimeBefore(User user,Long createTime);
    
    List<Finance> findByTypeOrderByCreateTimeDesc(Integer type);
}
