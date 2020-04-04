package com.lt.fly.Service.impl;

import com.lt.fly.Service.BaseService;
import com.lt.fly.Service.IFinanceService;
import com.lt.fly.Service.IOrderService;
import com.lt.fly.dao.IFinanceRepository;
import com.lt.fly.dao.IOrderRepository;
import com.lt.fly.entity.Order;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.DateUtil;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.web.query.BetReportFind;
import com.lt.fly.web.query.DetailsFind;
import com.lt.fly.web.query.OrderFind;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.resp.ReportResp;
import com.lt.fly.web.vo.BetReportVo;
import com.lt.fly.web.vo.DetailsVo;
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
        map.forEach((id, dto) -> {
            try {
                Order order = isNotNull(iOrderRepository.findById(id),null);
                if (dto.getBattleResult() > 0){
                    iFinanceService.add(order.getCreateUser(),dto.getBattleResult(),null, GlobalConstant.FinanceType.BET_RESULT);
                }
                order.setModifyTime(System.currentTimeMillis());
                order.setBattleResult(dto.getBattleResult());
                order.setExchangeDetail(dto.getExchangeDetail());
                order.setLotteryResult(dto.getLotteryResult());
                order.setResultType(GlobalConstant.ResultType.YES.getCode());
                order.setStatus(GlobalConstant.OrderStatus.CLEARING.getCode());
                iOrderRepository.save(order);
            } catch (ClientErrorException e) {
                e.printStackTrace();
            }
        });
        Long end = System.currentTimeMillis();
        Long time = end-start;
        System.err.println(""+ LocalDateTime.now()+">>>>>"+map.size()+"组数据结算的时长为:"+time+"ms");
    }

    //竞猜报表
    @Override
    public ReportResp findReport(BetReportFind query) throws ClientErrorException {
        if (null == query.getBefore()){
            query.setBefore(DateUtil.getDayStartTime(System.currentTimeMillis()));
        }
        if (null == query.getAfter()) {
            query.setAfter(System.currentTimeMillis());
        }

        List<BetReportVo> vos = new ArrayList<>();
        iFinanceRepository.findAll()
                .stream()
                .filter(finance -> finance.getCreateTime() < query.getAfter() &&
                        finance.getCreateTime() > query.getBefore())
                .collect(Collectors.groupingBy(Finance -> DateUtil.timestampToString(Finance.getCreateTime(), DateUtil.DEFAULT_FORMATS)))
                .forEach((s, finances) -> {
                    vos.add(new BetReportVo(s,finances));
                });
        List<BetReportVo> betReportVos = vos.stream()
                .sorted(new Comparator<BetReportVo>() {
                    @Override
                    public int compare(BetReportVo o1, BetReportVo o2) {
                        try {
                            Date d1 = DateUtil.parseDate(o1.getDateTime(), DateUtil.DEFAULT_FORMATS);
                            Date d2 = DateUtil.parseDate(o2.getDateTime(), DateUtil.DEFAULT_FORMATS);
                            return d2.compareTo(d1);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        return 0;
                    }
                })
                .skip((query.getPage()-1) * query.getSize())//分页
                .limit(query.getSize())
                .collect(Collectors.toList());

        ReportResp reportResp = new ReportResp(query.getPage(), vos.size(),(vos.size()  +  query.getSize() - 1) / query.getSize(), (long)vos.size(), betReportVos,vos);
        return reportResp;
    }

    @Override
    public PageResp details(DetailsFind query) throws ClientErrorException {
        //字符串专转成时间
        Date date = DateUtil.parseDate(query.getTime(),DateUtil.DEFAULT_FORMATS);
        //时间转时间戳
        long start = date.getTime();
        Long end = start + DateUtil.ONE_DAY_TIME;

        List<Order> orders = new ArrayList<>();
        if (null != query.getUserId()){
            orders = iOrderRepository.findByUser(query.getUserId());
        } else {
            orders = iOrderRepository.findAll();
        }
        List<DetailsVo> vos = new ArrayList<>();
        orders.stream()
                .filter(order -> order.getCreateTime()>start && order.getCreateTime()<end)
                .collect(Collectors.groupingBy(Order::getIssueNumber))
                .forEach((aLong, orders1) -> {
                    vos.add(new DetailsVo(orders1,query.getTime(),aLong));
                });
        List<DetailsVo> detailsVos = vos.stream()
                .sorted(Comparator.comparing(DetailsVo::getIssueNumber))
                .skip((query.getPage()-1) * query.getSize())//分页
                .limit(query.getSize())
                .collect(Collectors.toList());

        PageResp resp = new PageResp(query.getPage(), detailsVos.size(),(vos.size()+query.getSize()-1)/query.getSize(), (long)vos.size(),detailsVos);
        return resp;
    }
}
