package com.lt.fly.Service;


import com.lt.fly.web.req.UserLogin;
import com.lt.fly.web.resp.UserLoginResp;

public interface IUserService {

    UserLoginResp login(UserLogin req);  
}
