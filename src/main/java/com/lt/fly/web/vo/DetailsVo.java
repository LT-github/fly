package com.lt.fly.web.vo;

import com.lt.fly.entity.Order;
import com.lt.fly.utils.Arith;
import com.lt.fly.utils.GlobalConstant;
import lombok.Data;

import java.util.List;

@Data
public class DetailsVo {
    private String time;
    private Long issueNumber;
    private Double betTotal;
    private Double resultTotal;
    private Long count;

    public DetailsVo() {
    }

    public DetailsVo(List<Order> orders,String time,Long issueNumber) {
        this.betTotal = orders.stream()
                .filter(order -> !order.getStatus().equals(GlobalConstant.OrderStatus.CANCEL))
                .map(Order::getTotalMoney)
                .reduce(0.0,(a,b) -> Arith.add(a,b));
        double winMoney = orders.stream()
                .filter(order -> !order.getStatus().equals(GlobalConstant.OrderStatus.CANCEL))
                .map(Order::getBattleResult)
                .reduce(0.0,(a,b) -> Arith.add(a,b));
        this.resultTotal = Arith.sub(winMoney,betTotal);
        this.count = orders.stream()
                .filter(order -> !order.getStatus().equals(GlobalConstant.OrderStatus.CANCEL))
                .count();

        this.time = time;
        this.issueNumber = issueNumber;
    }

}
