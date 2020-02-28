package com.lt.fly.dao;



import com.lt.fly.entity.GameGroup;
import com.lt.fly.jpa.BaseRepository;

import java.util.List;

public interface IGameGroupRepository extends BaseRepository<GameGroup, Long> {

	GameGroup findByName(String name);
	
	List<GameGroup> findByType(String type);

	List<GameGroup> findByAddType(Integer addType);
	
}
