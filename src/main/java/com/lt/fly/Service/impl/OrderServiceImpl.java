package com.lt.fly.Service.impl;

import com.lt.fly.Service.BaseService;
import com.lt.fly.Service.IFinanceService;
import com.lt.fly.Service.IOrderService;
import com.lt.fly.dao.IFinanceRepository;
import com.lt.fly.dao.IOrderRepository;
import com.lt.fly.entity.Finance;
import com.lt.fly.entity.Order;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.Arith;
import com.lt.fly.utils.DateUtil;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.web.query.BetReportFind;
import com.lt.fly.web.query.OrderFind;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.resp.ReportResp;
import com.lt.fly.web.vo.BetReportVo;
import com.lt.fly.web.vo.OrderVo;
import com.lt.lxc.pojo.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends BaseService implements IOrderService {

    @Autowired
    private IOrderRepository iOrderRepository;

    @Autowired
    private IFinanceService iFinanceService;

    @Autowired
    private IFinanceRepository iFinanceRepository;

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
                if (map.get(key).getBattleResult() > 0) {
                    Order order = isNotNull(iOrderRepository.findById(map.get(key).getId()),"传递的参数没有实体类");
                    iFinanceService.add(order.getCreateUser(),map.get(key).getBattleResult(),null, GlobalConstant.FinanceType.BET_RESULT);
                }

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
    public ReportResp findReport(BetReportFind query) throws ClientErrorException {
        List<BetReportVo> vos = new ArrayList<>();
        iFinanceRepository.findAll()
                .stream()
                .filter(finance -> finance.getCreateTime() < query.getAfter() &&
                        finance.getCreateTime() > query.getBefore())
                .sorted(Comparator.comparing(Finance::getCreateTime,Comparator.reverseOrder()))//按时间降序
                .collect(Collectors.groupingBy(Finance -> DateUtil.timestampToString(Finance.getCreateTime(), DateUtil.DEFAULT_FORMATS)))
                .forEach((s, finances) -> {
                    vos.add(new BetReportVo(s,finances));
                });
        List<BetReportVo> betReportVos = vos.stream()
                .skip(query.getPage() * (query.getSize() - 1))//分页
                .limit(query.getSize())
                .collect(Collectors.toList());

        ReportResp reportResp = new ReportResp(query.getPage(), query.getSize(),(vos.size()  +  query.getSize() - 1) / query.getSize(), (long)vos.size(), betReportVos);

        reportResp.setData(vos);
        reportResp.setFenHongTotal(vos.stream().map(BetReportVo::getFengHong).reduce(0.0,(a,b) -> Arith.add(a,b)));
        reportResp.setHuiShuiTotal(vos.stream().map(BetReportVo::getHuiShui).reduce(0.0,(a,b) -> Arith.add(a,b)));
        reportResp.setDescendTotal(vos.stream().map(BetReportVo::getDescend).reduce(0.0,(a,b) -> Arith.add(a,b)));
        reportResp.setRechargeTotal(vos.stream().map(BetReportVo::getRecharge).reduce(0.0,(a,b) -> Arith.add(a,b)));
        reportResp.setWaterTotal(vos.stream().map(BetReportVo::getWater).reduce(0.0,(a,b) -> Arith.add(a,b)));
        reportResp.setWinMoneyTotal(vos.stream().map(BetReportVo::getWinMoney).reduce(0.0,(a,b) -> Arith.add(a,b)));
        reportResp.setBetResultTotal(vos.stream().map(BetReportVo::getBetResult).reduce(0.0,(a,b) -> Arith.add(a,b)));
        reportResp.setBetCountTotal(vos.stream().map(BetReportVo::getBetCount).reduce(0l,(a,b) -> a + b));


        return reportResp;
    }
}
