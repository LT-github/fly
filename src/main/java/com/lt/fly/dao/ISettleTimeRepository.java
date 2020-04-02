package com.lt.fly.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.lt.fly.entity.SettleTime;
import com.lt.fly.jpa.BaseRepository;

public interface ISettleTimeRepository extends BaseRepository<SettleTime, Long>{

	@Transactional
	@Modifying
	@Query(nativeQuery = true,value="update t_settle_time set status=0")
	void updateStatusAll();
}
