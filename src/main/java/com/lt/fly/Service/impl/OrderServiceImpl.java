package com.lt.fly.Service.impl;

import com.lt.fly.Service.IOrderService;
import com.lt.fly.dao.IOrderRepository;
import com.lt.fly.entity.Order;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.Arith;
import com.lt.fly.web.query.BetReportFind;
import com.lt.fly.web.query.OrderFind;
import com.lt.fly.web.resp.BetReportResp;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.BetReportVo;
import com.lt.fly.web.vo.OrderVo;
import com.lt.lxc.pojo.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
    public PageResp findAll(OrderFind req) throws ClientErrorException {
        Page<Order> page = iOrderRepository.findAll(req);
        PageResp resp = new PageResp(page);
        resp.setData(OrderVo.tovo(page.getContent()));
        return new PageResp(page);
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

    @Override
    public BetReportResp findReport(BetReportFind query) throws ClientErrorException {
        Pageable pageable = new PageRequest(query.getPage(), query.getSize());
        Page<Object[]> page = iOrderRepository.findReport(query.getStart(),query.getStart(),pageable);
        BetReportResp betReportResp = new BetReportResp(page);

        List<BetReportVo> betReportVos = new ArrayList<>();
        for (Object[] objArr:
             page.getContent()) {
            BetReportVo betReportVo = new BetReportVo();
            betReportVo.setDateTime(objArr[0].toString());
            //下注数
            betReportVo.setBetCount(Integer.parseInt(objArr[1].toString()));
            Arith.add(betReportResp.getBetCountTotal(),Integer.parseInt(objArr[1].toString()));
            //盈利
            betReportVo.setBetResult(Double.parseDouble(objArr[2].toString()));
            Arith.add(betReportResp.getBetResultTotal(),Double.parseDouble(objArr[2].toString()));
            //流水
            betReportVo.setWater(Double.parseDouble(objArr[3].toString()));
            Arith.add(betReportResp.getWaterTotal(),Double.parseDouble(objArr[3].toString()));
            //上分
            betReportVo.setRechargeCount(Double.parseDouble(objArr[4].toString()));
            Arith.add(betReportResp.getRechargeTotal(),Double.parseDouble(objArr[4].toString()));
            //下分
            betReportVo.setDescendCount(Double.parseDouble(objArr[5].toString()));
            Arith.add(betReportResp.getDescendTotal(),Double.parseDouble(objArr[5].toString()));
            //回水
            betReportVo.setHuiShui(Double.parseDouble(objArr[6].toString()));
            Arith.add(betReportResp.getHuiShuiTotal(),Double.parseDouble(objArr[6].toString()));
            //分红
            betReportVo.setFengHong(Double.parseDouble(objArr[7].toString()));
            Arith.add(betReportResp.getFenHongTotal(),Double.parseDouble(objArr[7].toString()));

            betReportVos.add(betReportVo);
        }

        betReportResp.setData(betReportVos);
        return betReportResp;
    }
}
