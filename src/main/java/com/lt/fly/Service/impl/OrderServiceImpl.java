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
    public PageResp<OrderVo,Order> findAll(OrderFind req) throws ClientErrorException {
        Page<Order> page = iOrderRepository.findAll(req);
        PageResp<OrderVo,Order> resp = new PageResp(page);
        resp.setData(OrderVo.tovo(page.getContent()));
        return resp;
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
                iOrderRepository.updateById(map.get(key).getLotteryResult(),map.get(key).getBattleResult(),map.get(key).getExchangeDetail(),key);
                issueNumberSet.add(map.get(key).getIssueNumber());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Long issueNumber :
                issueNumberSet) {
            iOrderRepository.updateResultTypeByIssuNumber(issueNumber);
            iOrderRepository.updateByIssueNumber(System.currentTimeMillis(),issueNumber);
        }
        Long end = System.currentTimeMillis();
        Long time = end-start;
        System.err.println(""+ LocalDateTime.now()+">>>>>"+map.size()+"组数据结算的时长为:"+time+"ms");
    }

    //竞猜报表
    @Override
    public BetReportResp findReport(BetReportFind query) throws ClientErrorException {
        PageRequest pageRequest = PageRequest.of(query.getPage(), query.getSize());//借助计算起始位置
        long total=iOrderRepository.countByReport(query.getStart(),query.getEnd());// 计算数据总条数
        List<Object[]> records=iOrderRepository.findReport(query.getStart(),query.getEnd(),pageRequest.getOffset(),pageRequest.getPageSize());// 获取分页数据
        int totalPageNum = (int)(total  +  query.getSize()  - 1) / query.getSize();//计算总页数

        BetReportResp betReportResp = new BetReportResp(query.getPage(), query.getSize(), totalPageNum, total, records);

        List<BetReportVo> betReportVos = new ArrayList<>();
        for (Object[] objArr:
                records) {
            BetReportVo betReportVo = new BetReportVo();
            betReportVo.setDateTime(objArr[0].toString());
            //下注数
            betReportVo.setBetCount((int) Math.floor(Double.parseDouble(objArr[1].toString())));
            betReportResp.setBetCountTotal(betReportResp.getBetCountTotal()+(int) Math.floor(Double.parseDouble(objArr[1].toString())));
            //盈利
            betReportVo.setBetResult(Double.parseDouble(objArr[2].toString()));
            betReportResp.setBetResultTotal(Arith.add(betReportResp.getBetResultTotal(),Double.parseDouble(objArr[2].toString())));
            //流水
            betReportVo.setWater(Double.parseDouble(objArr[3].toString()));
            betReportResp.setWaterTotal(Arith.add(betReportResp.getWaterTotal(),Double.parseDouble(objArr[3].toString())));
            //上分
            betReportVo.setRechargeCount(Double.parseDouble(objArr[4].toString()));
            betReportResp.setRechargeTotal(Arith.add(betReportResp.getRechargeTotal(),Double.parseDouble(objArr[4].toString())));
            //下分
            betReportVo.setDescendCount(Double.parseDouble(objArr[5].toString()));
            betReportResp.setDescendTotal(Arith.add(betReportResp.getDescendTotal(),Double.parseDouble(objArr[5].toString())));
            //回水
            betReportVo.setHuiShui(Double.parseDouble(objArr[6].toString()));
            betReportResp.setHuiShuiTotal(Arith.add(betReportResp.getHuiShuiTotal(),Double.parseDouble(objArr[6].toString())));
            //分红
            betReportVo.setFengHong(Double.parseDouble(objArr[7].toString()));
            betReportResp.setFenHongTotal(Arith.add(betReportResp.getFenHongTotal(),Double.parseDouble(objArr[7].toString())));

            betReportVos.add(betReportVo);
        }

        betReportResp.setData(betReportVos);
        return betReportResp;
    }
}
