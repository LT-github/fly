package com.lt.fly.web.controller;

import com.lt.fly.Service.IFinanceService;
import com.lt.fly.Service.IUserService;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IBetGroupRepository;
import com.lt.fly.dao.IFinanceRepository;
import com.lt.fly.dao.IOrderRepository;
import com.lt.fly.entity.*;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.redis.service.IOrderDTOServiceCache;
import com.lt.fly.utils.*;
import com.lt.fly.utils.gameUtils.GameProperty;
import com.lt.fly.utils.gameUtils.GameUtil;
import com.lt.fly.web.req.UserLogin;
import com.lt.fly.web.resp.ClientShowResp;
import com.lt.fly.web.req.FinanceAdd;
import com.lt.fly.web.req.OrderAdd;
import com.lt.fly.web.vo.FinanceVo;
import com.lt.fly.web.vo.OrderVo;
import com.lt.lxc.pojo.OrderDTO;
import com.lt.lxc.service.OpenDataISV;
import io.swagger.models.auth.In;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/client")
public class ClientController extends BaseController{

    @Autowired
    private IOrderRepository iOrderRepository;

    @Autowired
    private IBetGroupRepository iBetGroupRepository;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IFinanceService iFinanceService;

    @Autowired
    private IFinanceRepository iFinanceRepository;

    @Autowired
    private IOrderDTOServiceCache iOrderDTOServiceCache;

    @Autowired
    private IUserService iUserService;

    @Reference(version = "1.0.0",url="dubbo://localhost:23457",check = false)
    private OpenDataISV openData;


    @PostMapping("/login")
    public HttpResult login(@RequestBody UserLogin req) throws ClientErrorException{
        return HttpResult.success(iUserService.login(req),"登录成功");
    }


    @PostMapping("/bet")
    @UserLoginToken
    @Transactional(rollbackFor = ClientErrorException.class)
    public HttpResult bet(@RequestBody List<OrderAdd> reqs) throws ClientErrorException {
        //是否封盘
        if(!GlobalConstant.Bet.SWITCH.isFlag())
            throw new ClientErrorException("已封盘!");

        //找到登录会员
        Member member = (Member) getLoginUser();
        //会员余额
        double balance = iFinanceService.reckonBalance(ContextHolderUtil.getTokenUserId());
        if(0>=balance)
            throw new ClientErrorException("余额不足,请充值!");

        //实时返点比例值
        double proportion = 0;
        Set<Proportion> proportions = member.getHandicap().getProportions();
        if (null != proportions || !proportions.isEmpty()){
            for (Proportion item :
                    proportions) {
                if(item.getReturnPoint().getId().equals(CommonsUtil.TIMELY_LIUSHUI_RETURN_POINT)) {
                    proportion = (item.getProportionVal())/100;
                }
            }
        }

        //期号
        long issueNumber = GlobalConstant.NewData.ISSUE_NUMBER.getData();
        if(issueNumber == 1l) {
            //调用开奖系统的接口,得到数据库中最新一期的期号
            if(null == openData) {
                throw new ClientErrorException("开奖系统未启动!");
            }
            issueNumber = Long.parseLong(openData.getIssueNumber());
            GlobalConstant.NewData.ISSUE_NUMBER.setData(issueNumber);
        }

        double totalBet = 0;
        double totalReturn = 0;

        List<Order> orders = new ArrayList<>();
        //生成注单
        for (OrderAdd item : reqs) {

            Order order = new Order();
            //生成创建时间
            long nowtime = System.currentTimeMillis();
            order.setId(idWorker.nextId());
            order.setCreateTime(nowtime);
            order.setBetsContent(item.getBetsContent());

            //下注信息
            GameProperty gp = GameUtil.getTotalMoney(item.getBetsContent());
            order.setSingleBetting(gp.getSingle());
            order.setTotalMoney(gp.getMoney());
            order.setBetsCount(gp.getCount());

            //下注组
            BetGroup betGroup = isNotNull(iBetGroupRepository.findById(item.getBetGroupId()),"传递的下注组参数不存在");
            if(GlobalConstant.GameStatus.CLOSE.getCode() == betGroup.getStatus() || GlobalConstant.GameStatus.CLOSE.getCode() == betGroup.getGameGroup().getStatus()) {
                throw new ClientErrorException("该玩法已关闭!请检查后重试或联系管理员!");
            }
            if(!GameUtil.isRegex(betGroup.getGameGroup().getPingYinName(), item.getBetsContent())) {
                throw new ClientErrorException("下注格式不正确,请确认后重新下注!");
            }
            order.setBetGroup(betGroup);

            //赔率
            Handicap handicap = member.getHandicap();
            OddGroup oddGroup = handicap.getOddGroup();
            for (Odd odd :
                    oddGroup.getOdds()) {
                if (odd.getBetGroup().getId().equals(betGroup.getId())) {
                    if (null != odd.getSpecificOdd()){
                        order.setSpecificOdd(odd.getSpecificOdd());
                    }else {
                        order.setBetOdd(odd.getOddValue());
                    }
                }
                }

            if(balance<gp.getMoney()) {
                throw new ClientErrorException("余额不足,请充值!");
            }
            balance = balance-gp.getMoney()+gp.getMoney()*proportion;

            //财务记录集合
            Set<Finance> finances = new HashSet<>();
            FinanceAdd req = new FinanceAdd();

            //生成下注财务记录
            req.setMoney(gp.getMoney());
            req.setUserBalance(balance-gp.getMoney()*proportion);
            finances.add(iFinanceService.add(req,GlobalConstant.FananceType.BET.getCode()));

            //生成返点财务记录
            req.setMoney(gp.getMoney()*proportion);
            req.setUserBalance(balance);
            finances.add(iFinanceService.add(req,GlobalConstant.FananceType.TIMELY_LIUSHUI.getCode()));
            //添加财务记录
            order.setFinances(finances);

            //下注总额
            totalBet += gp.getMoney();
            //总返点
            totalReturn +=gp.getMoney()*proportion;

            //补全order基本信息
            order.setId(idWorker.nextId());
            order.setIssueNumber(issueNumber);
            order.setLotteryType(betGroup.getGameGroup().getType());
            order.setCreateUser(member);
            order.setCreateTime(nowtime);

            orders.add(order);
        }
        iOrderRepository.saveAll(orders);
        //是否封盘
        if(!GlobalConstant.Bet.SWITCH.isFlag())
            throw new ClientErrorException("已封盘!");
        else{
            //orderDTO集合
            List<OrderDTO> orderDTOS = new ArrayList<>();

            for (Order item :
                    orders) {
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(item,orderDTO);
                orderDTO.setGame(item.getBetGroup().getGameGroup().getPingYinName());
                orderDTO.setOdds(item.getBetOdd());
                if (null != item.getSpecificOdd()){
                    orderDTO.setSpecificOdds(item.getSpecificOdd());
                }
                orderDTOS.add(orderDTO);
            }
            //存入OrderDTO缓存
            iOrderDTOServiceCache.add(orderDTOS);
        }
        return HttpResult.success(OrderVo.tovo(orders),"下注成功!");
    }

    /**
     * 充值
     * @return
     * @throws ClientErrorException
     */
    @PostMapping("/recharge")
    @UserLoginToken
    public HttpResult recharge(@RequestBody FinanceAdd req) throws ClientErrorException{
        Finance finance = iFinanceService.add(req,GlobalConstant.FananceType.RECHARGE.getCode());
        iFinanceRepository.save(finance);
        return HttpResult.success(new FinanceVo(finance),"上分申请提交成功");
    }

    /**
     * 下分
     * @return
     * @throws ClientErrorException
     */
    @PostMapping("/descend")
    @UserLoginToken
    public HttpResult descend(@RequestBody FinanceAdd req) throws ClientErrorException{
        //先查余额
        double balance = iFinanceService.reckonBalance(ContextHolderUtil.getTokenUserId());
        if(balance<req.getMoney()){
            throw new ClientErrorException("余额不足");
        }
        Finance finance = iFinanceService.add(req,GlobalConstant.FananceType.DESCEND.getCode());
        iFinanceRepository.save(finance);
        return HttpResult.success(new FinanceVo(finance),"下分申请提交成功");
    }

    /**
     * 首页显示
     * @return
     */
    @GetMapping
    @UserLoginToken
    public HttpResult index(ClientShowResp resp) throws ClientErrorException{
        //现在时间戳
        long now = System.currentTimeMillis();
        //获取今日零点时间戳
        long zero=now/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();

        List<Order> orders = iOrderRepository.findByUserAndTime(now,zero,ContextHolderUtil.getTokenUserId());

        BigDecimal betResult = new BigDecimal(0);
        BigDecimal betTotal = new BigDecimal(0);
        BigDecimal returnTotal = new BigDecimal(0);
        BigDecimal dividendTotal = new BigDecimal(0);
        Set<Long> issues = new HashSet<>();
        for (Order item :
                orders) {
            betTotal.add(new BigDecimal(item.getTotalMoney()));
            if(item.getBattleResult()<0)
                betResult.subtract(new BigDecimal(item.getBattleResult()));
            else
                betResult.add(new BigDecimal(item.getBattleResult()));

            issues.add(item.getIssueNumber());
            for (Finance it:
                 item.getFinances()) {
                if(it.getType().equals(GlobalConstant.FananceType.RANGE_LIUSHUI)){
                    returnTotal.add(new BigDecimal(it.getMoney()));
                }
                if(it.getType().equals(GlobalConstant.FananceType.RANGE_YINGLI)){
                    dividendTotal.add(new BigDecimal(it.getMoney()));
                }
            }
        }
        resp.setBetResult(betResult.doubleValue());
        resp.setBetTotal(betTotal.doubleValue());
        resp.setIssueCount(issues.size());
        resp.setReturnTotal(returnTotal.doubleValue());
        resp.setDividendTotal(dividendTotal.doubleValue());
        return HttpResult.success(resp,"查询今日数据成功");
    }


    @GetMapping("/balance")
    @UserLoginToken
    public HttpResult findBalance() throws ClientErrorException{
        return HttpResult.success(iFinanceService.reckonBalance(ContextHolderUtil.getTokenUserId()),"查询成功");
    }


}
