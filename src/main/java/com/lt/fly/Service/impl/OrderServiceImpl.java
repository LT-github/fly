package com.lt.fly.Service.impl;

import com.lt.fly.Service.IOrderService;
import com.lt.fly.dao.IOrderRepository;
import com.lt.fly.entity.Order;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.web.req.OrderFind;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.OrderVo;
import com.lt.lxc.pojo.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /**
     * 注单结算
     * @param map
     */
    @Override
    public void settle(Map<Long, OrderDTO> map) {
        Long start =  System.currentTimeMillis();
        Set<Long> issueNumberSet = new HashSet<>();
        //修改betOrder
        for ( Long key : map.keySet()) {
            try {


//                iOrderRepository.updateById(map.get(key).getLotteryResult(),map.get(key).getBattleResult(),map.get(key).getExchangeDetail(),key);
                issueNumberSet.add(map.get(key).getIssueNumber());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Long end = System.currentTimeMillis();
        Long time = end-start;
        System.err.println(""+ LocalDateTime.now()+">>>>>"+map.size()+"组数据结算的时长为:"+time+"ms");
    }
}
