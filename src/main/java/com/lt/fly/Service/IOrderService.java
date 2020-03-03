package com.lt.fly.Service;

import com.lt.fly.entity.Order;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.web.req.OrderAdd;
import com.lt.fly.web.req.OrderFind;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.OrderVo;

public interface IOrderService {

    PageResp<OrderVo, Order> findAll(OrderFind req) throws ClientErrorException;
}