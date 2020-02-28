package com.lt.fly.dao;

import com.lt.fly.entity.User;
import com.lt.fly.jpa.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends BaseRepository<User, Long> {

	User findByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE u.id = :id")
	User findByUserId(Long id);
	

	
}
