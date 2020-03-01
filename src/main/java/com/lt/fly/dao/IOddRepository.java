package com.lt.fly.dao;

import com.lt.fly.entity.BetGroup;
import com.lt.fly.entity.Odd;
import com.lt.fly.jpa.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOddRepository extends BaseRepository<Odd,Long> {

    @Query("select odd from Odd odd where  odd.id in ?1")
    List<Odd> findByIds(List<Long> ids);

    @Query(nativeQuery = true,value = "select * from t_odd as o where o.bet_group_id = ?1 " +
            "and o.id = (select t.odd_id from t_odd_group_odd as t where  t.odd_group_id = ?2)")
    Odd findOne(Long betGroupId,Long oddGroupId);

    @Query(nativeQuery = true,value = "select * from t_odd o where o.bet_group_id = ?1")
    List<Odd> findByBetGroupId(Long betGroupId);
}
