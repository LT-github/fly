package com.lt.fly.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lt.fly.entity.Finance;
import com.lt.fly.entity.User;
import com.lt.fly.jpa.BaseRepository;


@Repository
public interface IFinanceRepository extends BaseRepository<Finance, Long> {

    //查用户余额
    List<Finance> findAllByCreateUser(User user);

    //查找用户流水财务，某个时间段
    List<Finance> findByCreateTimeBetweenAndCreateUserAndType(Long before, Long after, User user, Integer type);

    //计算某个用户的余额
    @Query(nativeQuery = true, value = "SELECT (ifnull(a.snum1,0)+ifnull(b.snum2,0)-ifnull(c.snum3,0)+ifnull(d.snum4,0)+ifnull(e.snum5,0)+ifnull(f.snum6,0)) " +
            "FROM " +
            "(SELECT sum(ifnull(money,0)) snum1 from t_finance WHERE  status=0 AND audit_status=1 AND type=1 AND create_user_id=?1) a," +
            "(SELECT sum(ifnull(money,0)) snum2 from t_finance WHERE status=0 AND type=2 AND create_user_id=?1) b," +
            "(SELECT sum(ifnull(money,0)) snum3 from t_finance WHERE status=0 AND type=3 AND create_user_id=?1) c," +
            "(SELECT sum(ifnull(money,0)) snum4 from t_finance WHERE status=0 AND type=4 AND create_user_id=?1) d," +
            "(SELECT sum(ifnull(money,0)) snum5 from t_finance WHERE status=0 AND type=5 AND create_user_id=?1) e," +
            "(SELECT sum(ifnull(money,0)) snum6 from t_finance WHERE status=0 AND type=6 AND create_user_id=?1) f ")
    Double findMemberBlance(Long memberId);

    //计算某个用户，某个时间点的余额
    @Query(nativeQuery = true, value = "SELECT (ifnull(a.snum1,0)+ifnull(b.snum2,0)-ifnull(c.snum3,0)+ifnull(d.snum4,0)+ifnull(e.snum5,0)+ifnull(f.snum6,0)) " +
            "FROM " +
            "(SELECT sum(ifnull(money,0)) snum1 from t_finance WHERE  status=0 AND audit_status=1 AND type=1 AND create_user_id=?1 AND create_time <=?2 ) a," +
            "(SELECT sum(ifnull(money,0)) snum2 from t_finance WHERE status=0 AND type=2 AND create_user_id=?1 AND create_time <=?2) b," +
            "(SELECT sum(ifnull(money,0)) snum3 from t_finance WHERE status=0 AND type=3 AND create_user_id=?1 AND create_time <=?2) c," +
            "(SELECT sum(ifnull(money,0)) snum4 from t_finance WHERE status=0 AND type=4 AND create_user_id=?1 AND create_time <=?2) d," +
            "(SELECT sum(ifnull(money,0)) snum5 from t_finance WHERE status=0 AND type=5 AND create_user_id=?1 AND create_time <=?2) e," +
            "(SELECT sum(ifnull(money,0)) snum6 from t_finance WHERE status=0 AND type=6 AND create_user_id=?1 AND create_time <=?2) f ")
    Double findMemberBlanceByTime(Long memberId, Long after);


    //查今日财务记录
	List<Finance> findByCreateTimeBetweenAndCreateUser(Long start,Long end,User user);


}
