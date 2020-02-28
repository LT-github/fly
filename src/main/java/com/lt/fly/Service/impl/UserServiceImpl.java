package com.lt.fly.Service.impl;

import com.lt.fly.Service.IUserService;
import com.lt.fly.dao.IUserRepository;
import com.lt.fly.entity.User;
import com.lt.fly.utils.ContextHolderUtil;
import com.lt.fly.utils.GlobalUtil;
import com.lt.fly.utils.TokenUtil;
import com.lt.fly.web.req.UserLogin;
import com.lt.fly.web.resp.UserLoginResp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public UserLoginResp login(UserLogin req) {
        User dbUser = iUserRepository.findByUsername(req.getUsername());
        if(dbUser == null )
            throw new RuntimeException("登录失败,用户不存在");

        if(!StringUtils.equals(req.getPassword(), dbUser.getPassword()))
            throw new RuntimeException("登录失败，密码错误");
        String token = tokenUtil.getToken(dbUser, GlobalUtil.getCliectIp(ContextHolderUtil.getRequest()));
        return new UserLoginResp(dbUser.getId(), dbUser.getNickname(), dbUser.getUsername(),token);
    }
}
