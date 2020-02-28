package com.lt.fly.dao;

import com.lt.fly.entity.Admin;
import com.lt.fly.jpa.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdminRepository extends BaseRepository<Admin, Long> {

	Admin findByUsername(String username);
	
}
