package com.lt.fly.dao;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.web.query.BetReportFind;
import com.lt.fly.web.resp.BetReportResp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lt.fly.entity.Order;
import com.lt.fly.entity.User;
import com.lt.fly.jpa.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IOrderRepository extends BaseRepository<Order, Long> {
	
	@Query(nativeQuery = true,value = "select * from t_order where create_user_id = :memberId and create_time between :start and :end")
	List<Order> findByUserAndTime(Long memberId,Long start,Long end);

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
