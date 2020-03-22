package com.lt.fly.dao;


import com.lt.fly.entity.OddGroup;
import com.lt.fly.jpa.BaseRepository;

public interface IOddGroupRepository extends BaseRepository<OddGroup,Long> {

    OddGroup findByName(String name);
}
