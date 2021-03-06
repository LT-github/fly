package com.lt.fly.dao;

import com.lt.fly.entity.Member;
import com.lt.fly.entity.User;
import com.lt.fly.jpa.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMemberRepository extends BaseRepository<Member,Long> {

    Member findByUsername(String username);

    List<Member> findByModifyUser(User user);

    Member findByReferralCode(String referralCode);
}
