package com.lt.fly.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lt.fly.entity.Order;
import com.lt.fly.jpa.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IOrderRepository extends BaseRepository<Order, Long> {
	
	@Query(nativeQuery = true,value = "select * from t_order where create_user_id = :userId")
	List<Order> findByUser(Long userId);

	@Modifying
	@Transactional
	@Query("update Order set status = 1, lotteryResult =?1, battleResult =?2, exchangeDetail =?3 where id = ?4")
	void updateById(int lotteryResult,double battleResult,String exchangeDetail,long id);

	@Modifying
	@Transactional
	@Query("update Order o set o.status = 1 ,o.modifyTime = ?1 where o.issueNumber = ?2 and o.status = 0")
	void updateByIssueNumber(Long modifyTime , Long issueNumber);

	@Modifying
	@Transactional
	@Query("update Order o set o.resultType = 1 where o.issueNumber = ?1 ")
	void updateResultTypeByIssuNumber(Long issueNumber);
	
}
