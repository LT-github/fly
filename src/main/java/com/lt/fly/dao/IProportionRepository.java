package com.lt.fly.dao;

import com.lt.fly.entity.Proportion;
import com.lt.fly.jpa.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProportionRepository extends BaseRepository<Proportion, Long> {

    Proportion findByDescription(String description);
}
