package com.lt.fly.Service.impl;

import com.lt.fly.Service.IGameGroupService;
import com.lt.fly.dao.IGameGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameGroupServiceImpl implements IGameGroupService {

    @Autowired
    private IGameGroupRepository iGameGroupRepository;

}
