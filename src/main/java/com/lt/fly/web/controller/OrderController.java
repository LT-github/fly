package com.lt.fly.web.controller;

import com.lt.fly.Service.IOrderService;
import com.lt.fly.annotation.RequiredPermission;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IOrderRepository;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.req.OrderAdd;
import com.lt.fly.web.req.OrderFind;
import com.lt.lxc.pojo.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
