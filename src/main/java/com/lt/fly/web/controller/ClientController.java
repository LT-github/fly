package com.lt.fly.web.controller;

import com.lt.fly.Service.IFinanceService;
import com.lt.fly.Service.IUserService;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IBetGroupRepository;
import com.lt.fly.dao.IFinanceRepository;
import com.lt.fly.dao.IMemberRepository;
import com.lt.fly.dao.IOrderRepository;
import com.lt.fly.entity.*;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.redis.service.IOrderDTOServiceCache;
import com.lt.fly.utils.*;
import com.lt.fly.utils.gameUtils.GameProperty;
import com.lt.fly.utils.gameUtils.GameUtil;
import com.lt.fly.web.req.*;
import com.lt.fly.web.resp.ClientShowResp;
import com.lt.fly.web.vo.FinanceVo;
import com.lt.fly.web.vo.MemberVo;
import com.lt.fly.web.vo.OrderVo;
import com.lt.lxc.pojo.OrderDTO;
import com.lt.lxc.service.OpenDataISV;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.lt.fly.utils.GlobalConstant.FananceType.*;

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

    @Autowired
    private IMemberRepository iMemberRepository;

    @Reference(version = "1.0.0",url="dubbo://localhost:23457",check = false)
    private OpenDataISV openData;


    /**
     * 登录
     * @param req
     * @return
     * @throws ClientErrorException
     */
    @PostMapping("/login")
    public HttpResult login(@RequestBody UserLogin req) throws ClientErrorException{
        return HttpResult.success(iUserService.login(req),"登录成功");
    }

    /**
     * 注册
     * @param obj
     * @return
     * @throws ClientErrorException
     */
    @PostMapping("/register")
    public HttpResult register(@RequestBody @Validated MemberAddByClient obj ,
                               BindingResult bindingResult)throws ClientErrorException{
        this.paramsValid(bindingResult);
        Member member = new Member();
        member.setId(idWorker.nextId());
        member.setCreateTime(System.currentTimeMillis());
        member.setCreateUser(this.getLoginUser());
        BeanUtils.copyProperties(obj, member);
        if (null == obj.getNickname()){
            member.setNickname(obj.getUsername());
        }

        if (null != obj.getReferralCode()){
            Long memberId = ShareCodeUtil.codeToId(obj.getReferralCode());
            Member modifyUser = isNotNull(iMemberRepository.findById(memberId),"邀请码不正确!");
            member.setModifyUser(modifyUser);
        }

        member.setCreateTime(System.currentTimeMillis());

        iMemberRepository.save(member);
        return HttpResult.success(new MemberVo(member),"注册成功!");
    }

    @PostMapping("/update")
    @UserLoginToken
    public HttpResult update(@PathVariable Long id , @RequestBody @Validated MemberEditByClient obj ,
                             BindingResult bindingResult) throws ClientErrorException{
        this.paramsValid(bindingResult);

        Member objEdit = isNotNull(iMemberRepository.findById(id),"会员id查询不到实体");
        BeanUtils.copyProperties(obj, objEdit);
        if (null == obj.getNickname())
            objEdit.setNickname(objEdit.getNickname());


        objEdit.setModifyUser(getLoginUser());
        objEdit.setModifyTime(System.currentTimeMillis());

        iMemberRepository.save(objEdit);

        return HttpResult.success(new MemberVo(objEdit), "编辑成功");
    }

    /**
     * 下注
     * @param reqs
     * @return
     * @throws ClientErrorException
     */
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
                    proportion = Arith.div(item.getProportionVal(),100,2);
                }
            }
        }

        //期号
        long issueNumber = GlobalConstant.NewData.ISSUE_NUMBER.getData();
        if(issueNumber == 1l) {
            //调用开奖系统的接口,得到当前期的期号
            if(null == openData) {
                throw new ClientErrorException("开奖系统未启动!");
            }
            issueNumber = Long.parseLong(openData.getIssueNumber());
            GlobalConstant.NewData.ISSUE_NUMBER.setData(issueNumber);
        }

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

            //补全order基本信息
            order.setId(idWorker.nextId());
            order.setIssueNumber(issueNumber);
            order.setLotteryType(betGroup.getGameGroup().getType());
            order.setCreateUser(member);
            order.setCreateTime(nowtime);

            //生成下注财务记录
            iFinanceService.add(member,gp.getMoney(),balance, BET);

            //生成返点财务记录
            iFinanceService.add(member,Arith.mul(gp.getMoney(),proportion),Arith.sub(balance,gp.getMoney()), TIMELY_LIUSHUI);
            orders.add(order);

            balance = Arith.sub(balance,gp.getMoney())+gp.getMoney()*proportion;
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
        Finance finance = iFinanceService.add((Member) getLoginUser(),req.getMoney(),null, RECHARGE);
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
        Finance finance = iFinanceService.add((Member) getLoginUser(),req.getMoney(),null, DESCEND);
        iFinanceRepository.save(finance);
        return HttpResult.success(new FinanceVo(finance),"下分申请提交成功");
    }

    /**
     * 首页显示
     * @return
     */
    @GetMapping
    @UserLoginToken
    public HttpResult index() throws ClientErrorException{
        //现在时间戳
        long now = System.currentTimeMillis();
        //获取今日零点时间戳
        long zero = DateUtil.getDayStartTime(System.currentTimeMillis());

        List<Order> orders = iOrderRepository.findByUserAndTime(ContextHolderUtil.getTokenUserId(),zero,now);

        List<Finance> finances = iFinanceRepository.findByCreateTimeBetweenAndCreateUser(zero,now,getLoginUser());


        double betResult = 0;
        double betTotal = 0;
        double timelyTotal = 0;
        double rangeTotal = 0;
        double dividendTotal = 0;
        for (Finance item :
                finances) {
            //下注金额
            if (item.getType().equals(BET.getCode())) {
                betTotal = Arith.add(betTotal,item.getMoney());
            }
            //飞单盈亏
            if(item.getType().equals(BET_WIN.getCode())){
                betResult = Arith.add(betResult,item.getMoney());

            }
            //区间流水
            if (item.getType().equals(RANGE_LIUSHUI.getCode())) {
                rangeTotal = Arith.add(rangeTotal,item.getMoney());
            }
            //区间分红
            if (item.getType().equals(RANGE_YINGLI.getCode())) {
                dividendTotal = Arith.add(dividendTotal,item.getMoney());
            }
            //实时流水
            if (item.getType().equals(TIMELY_LIUSHUI.getCode())) {
                timelyTotal = Arith.add(timelyTotal,item.getMoney());
            }
            //撤销
            if(item.getType().equals(CANCLE.getCode())){
                timelyTotal = Arith.sub(timelyTotal,item.getMoney());
            }
        }
        Set<Long> issues = new HashSet<>();

        for (Order item :
                orders) {
            issues.add(item.getIssueNumber());
        }

        ClientShowResp resp = new ClientShowResp();
        resp.setBetResult(Arith.sub(betResult,betTotal));
        resp.setBetTotal(betTotal);
        resp.setIssueCount(issues.size());
        resp.setRangeTotal(rangeTotal);
        resp.setTimelyTotal(timelyTotal);
        resp.setDividendTotal(dividendTotal);
        return HttpResult.success(resp,"查询今日数据成功");
    }


    @GetMapping("/balance")
    @UserLoginToken
    public HttpResult findBalance() throws ClientErrorException{
        return HttpResult.success(iFinanceService.reckonBalance(ContextHolderUtil.getTokenUserId()),"查询成功");
    }


}
