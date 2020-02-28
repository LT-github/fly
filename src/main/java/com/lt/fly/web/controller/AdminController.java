package com.lt.fly.web.controller;

import com.lt.fly.Service.IUserService;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.req.UserLogin;
import com.lt.fly.web.resp.UserLoginResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @Autowired
    private IUserService iUserService;


    @Autowired
    private IdWorker idWorker;

    //系统用户登录
    @PostMapping("/login")
    public HttpResult<UserLoginResp> login(@RequestBody UserLogin req) {
        return HttpResult.success(iUserService.login(req),"登录成功");
    }

}
