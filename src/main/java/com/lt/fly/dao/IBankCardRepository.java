package com.lt.fly.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.lt.fly.entity.BankCard;
import com.lt.fly.jpa.BaseRepository;



public interface IBankCardRepository extends BaseRepository<BankCard, Long>{

	@Transactional
	@Modifying
	@Query(nativeQuery = true,value="update t_bank_card set status=0 where status=1")
    void updateStatusAll();
	BankCard findByStatus(Integer status);
}
