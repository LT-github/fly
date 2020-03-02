package com.lt.fly.web.vo;

import com.lt.fly.entity.Order;
import com.lt.fly.utils.MyBeanUtils;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderVo {

    private String username;//用户名

    private String lotteryType;  //彩种

    private Long issueNumber;//期号

    private Integer resultType;//是否开奖

    private Integer status;//是否结算

    private Long createTime;//竞猜时间

    private Long modifyTime;//结算时间

    private Long id;//单号

    private String gameType;//类型

    private String betsContent;//竞猜内容

    private Double odds;//赔率

    private Double singleBetting;//单注金额

    private Double totalMoney;//总点

    private Integer lotteryResult;//战役

    private Double battleResult;//战果,输赢的金额

    public OrderVo() {
        super();
    }

    public OrderVo(Order obj) {
        BeanUtils.copyProperties(obj,this);
        this.username = obj.getCreateUser().getUsername();
        this.gameType = obj.getBetGroup().getGameGroup().getName();
    }
    public static List<OrderVo> tovo(List<Order> orders){
        List<OrderVo> orderVos = new ArrayList<>();
        for (Order item :
                orders) {
            orderVos.add(new OrderVo(item));
        }
        return orderVos;
    }
}
