package com.lt.fly.dao;

import com.lt.fly.entity.Role;
import com.lt.fly.jpa.BaseRepository;

public interface IRoleRepository extends BaseRepository<Role, Long> {

    Role findByName(String name);
}
