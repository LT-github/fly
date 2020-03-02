//package com.lt.fly.web.controller;
//
//import com.lt.fly.annotation.UserLoginToken;
//import com.lt.fly.dao.IAdminRepository;
//import com.lt.fly.dao.IBetGroupRepository;
//import com.lt.fly.dao.IOrderRepository;
//import com.lt.fly.entity.*;
//import com.lt.fly.exception.ClientErrorException;
//import com.lt.fly.utils.*;
//import com.lt.fly.utils.gameUtils.GameProperty;
//import com.lt.fly.utils.gameUtils.GameUtil;
//import com.lt.fly.web.req.OrderAdd;
//import com.lt.lxc.service.OpenDataISV;
//import com.sun.xml.internal.bind.v2.model.core.ID;
//import org.apache.catalina.Store;
//import org.apache.dubbo.config.annotation.Reference;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import sun.security.krb5.internal.Ticket;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@RestController
//@RequestMapping("/operation")
//public class OperationController extends BaseController{
//
//    @Autowired
//    private IOrderRepository iOrderRepository;
//
//    @Autowired
//    private IBetGroupRepository iBetGroupRepository;
//
//    @Autowired
//    private IdWorker idWorker;
//
//    @Reference(version = "1.0.0",url="dubbo://localhost:23457",check = false)
//    private OpenDataISV openData;
//
//    @PostMapping("/bet")
//    @UserLoginToken
//    public HttpResult bet(@RequestBody List<OrderAdd> reqs) throws ClientErrorException {
//        //是否封盘
//        if(!GlobalConstant.Bet.SWITCH.isFlag())
//            throw new ClientErrorException("已封盘!");
//
//        //找到登录会员
//        Member member = (Member) getLoginUser();
//        //会员余额
//        double balance = 0;
//        if(0>=balance)
//            throw new ClientErrorException("余额不足,请充值!");
//
//        //实时返点比例值
//        double proportion = 0;
//        Set<Proportion> proportions = member.getHandicap().getProportions();
//        if (null != proportions || !proportions.isEmpty()){
//            for (Proportion item :
//                    proportions) {
//                if(item.getReturnPoint().getId().equals(CommonsUtil.TIMELY_LIUSHUI_RETURN_POINT)) {
//                    proportion = (item.getProportionVal())/100;
//                }
//            }
//        }
//
//        //期号
//        long issueNumber = GlobalConstant.NewData.ISSUE_NUMBER.getData();
//        if(issueNumber == 1l) {
//            //调用开奖系统的接口,得到数据库中最新一期的期号
//            if(null == openData) {
//                throw new ClientErrorException("开奖系统未启动!");
//            }
//            issueNumber = Long.parseLong(openData.getIssueNumber());
//            GlobalConstant.NewData.ISSUE_NUMBER.setData(issueNumber);
//        }
//
//        for (OrderAdd item : reqs) {
//            Order order = new Order();
//            //生成创建时间
//            long nowtime = System.currentTimeMillis();
//            order.setId(idWorker.nextId());
//            order.setCreateTime(nowtime);
//            order.setBetsContent(item.getBetsContent());
//
//            GameProperty gp = GameUtil.getTotalMoney(item.getBetsContent());
//            order.setSingleBetting(gp.getSingle());
//            order.setTotalMoney(gp.getMoney());
//            order.setBetsCount(gp.getCount());
//
//            //下注组
//            BetGroup betGroup = isNotNull(iBetGroupRepository.findById(item.getBetGroupId()),"传递的下注组参数不存在");
//            if(GlobalConstant.GameStatus.CLOSE.getCode() == betGroup.getStatus() || GlobalConstant.GameStatus.CLOSE.getCode() == betGroup.getGameGroup().getStatus()) {
//                throw new ClientErrorException("该玩法已关闭!请检查后重试或联系管理员!");
//            }
//            if(!GameUtil.isRegex(betGroup.getGameGroup().getPingYinName(), item.getBetsContent())) {
//                throw new ClientErrorException("下注格式不正确,请确认后重新下注!");
//            }
//            order.setBetGroup(betGroup);
//
//            //赔率
//            Handicap handicap = member.getHandicap();
//            OddGroup oddGroup = handicap.getOddGroup();
//            for (Odd odd :
//                    oddGroup.getOdds()) {
//                if (odd.getBetGroup().getId().equals()) {
//                }
//
//            if(bl<gp.getMoney()) {
//                throw new ClientErrorException("余额不足,请充值!");
//            }
//            bl = bl-gp.getMoney()+gp.getMoney()*proportion;
//
//            //生成下注财务记录
//            BetsFinance bf = new BetsFinance();
//            bf.setId(idWorker.nextId());
//            bf.setCreateTime(nowtime);
//            bf.setMoney(gp.getMoney());
//            bf.setHolder(s);
//            bf.setBetsOrder(bo);
//            bf.setBalance(bl-gp.getMoney());
//            bf.setDescription(bor.getDescription());
//
//            //生成返点财务记录
//            ReturnPointFinance rpf = new ReturnPointFinance();
//            rpf.setId(idWorker.nextId());
//            rpf.setCreateTime(nowtime);
//            rpf.setMoney(gp.getMoney()*proportion);
//            rpf.setHolder(s);
//            rpf.setBalance( bl-gp.getMoney()+gp.getMoney()*proportion);
//            rpf.setDescription("实时返点");
//
//            //赋值给订单
//            bo.setBetsFinance(bf);
//            bo.setReturnPointFinance(rpf);
//            bo.setTicket(t);
//
//            set.add(bo);
//            //计算这张票的总额
//            totalMoney += gp.getMoney();
//            //计算这张彩票的总返点
//            returnMoney +=gp.getMoney()*proportion;
//
//            t.setBetsOrders(set);
//            //补全ticket属性
//            Long id = idWorker.nextId();
//            //获取条形码base64字符串
//            String img = getBase64(id);
//            t.setId(id);
//            t.setIssueNumber(issueNumber);
//            t.setBarCode(id+"");
//            t.setDeadline((deadline*24*60*60*1000)+nowtime);
//            t.setLotteryType(gameContent.getGame().getType());
//            t.setBettingUser(s);
//            t.setCreateTime(nowtime);
//            //扣除订单费用,加上实时返点金额
//            t.getBettingUser().setBalance(bl-totalMoney+returnMoney);
////			try {
////				iTicketServiceCache.add(t);
////			} catch (Exception e) {
////				throw new ClientErrorException("缓存出现异常"+e.getMessage());
////			}
//            //存入内存中
//            TICKETMAP.put(id, t);
//
//            TicketVo vo = new TicketVo(t);
//            vo.setBarCodeImg(img);
//            vo.setMoney(totalMoney);
//            vo.setOpenTime(DateUtil.getTimeToMin(nowtime, 5));
//            vos.add(vo);
//        }
//
//        return vos;
//        return HttpResult.success(null,"下注成功!");
//    }
//}
