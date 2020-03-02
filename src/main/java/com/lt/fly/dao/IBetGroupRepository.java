package com.lt.fly.dao;


import com.lt.fly.entity.BetGroup;
import com.lt.fly.jpa.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IBetGroupRepository extends BaseRepository<BetGroup, Long> {

}
