package com.lt.fly.dao;

import java.util.List;

import com.lt.fly.entity.Handicap;
import com.lt.fly.jpa.BaseRepository;

public interface IHandicapRepository extends BaseRepository<Handicap,Long> {

    Handicap findByName(String name);
    List<Handicap> findAllByIdsAndSettlementType(List<Long> ids,Integer settlementType);
    List<Handicap> findAllBySettlementType(Integer settlementType);
}
