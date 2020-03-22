package com.lt.fly.dao;

import com.lt.fly.entity.Handicap;
import com.lt.fly.jpa.BaseRepository;

public interface IHandicapRepository extends BaseRepository<Handicap,Long> {

    Handicap findByName(String name);
}
