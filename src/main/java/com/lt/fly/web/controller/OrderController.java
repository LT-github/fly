package com.lt.fly.web.controller;

import com.lt.fly.Service.IOrderService;
import com.lt.fly.annotation.RequiredPermission;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.web.query.OrderFind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Autowired
    private IOrderService iOrderService;

    /**
     * 竞猜列表
     * @param req
     * @return
     * @throws ClientErrorException
     */
    @RequiredPermission(value="findGuessingList")
    @PostMapping("/all")
    @UserLoginToken
    public HttpResult findAll(@RequestBody OrderFind req) throws ClientErrorException {
        return HttpResult.success(iOrderService.findAll(req),"查询竞猜记录列表成功");
    }

}
