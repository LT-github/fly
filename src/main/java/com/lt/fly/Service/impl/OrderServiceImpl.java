package com.lt.fly.Service.impl;

import com.lt.fly.Service.IOrderService;
import com.lt.fly.dao.IOrderRepository;
import com.lt.fly.entity.Order;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.web.req.OrderFind;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderRepository iOrderRepository;

    /**
     * 竞猜记录
     * @param req
     * @return
     * @throws ClientErrorException
     */
    @Override
    public PageResp<OrderVo, Order> findAll(OrderFind req) throws ClientErrorException {
        Page<Order> page = iOrderRepository.findAll(req);

        return new PageResp<OrderVo,Order>(page).getPageVo(new PageResp.PageGenerator<OrderVo,Order>(){

            @Override
            public List<OrderVo> generator(List<Order> content) {
                return OrderVo.tovo(content);
            }
        });
    }
}
