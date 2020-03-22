package com.lt.fly.dao;

import com.lt.fly.entity.Member;
import com.lt.fly.jpa.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMemberRepository extends BaseRepository<Member,Long> {

    Member findByName(String name);
}
