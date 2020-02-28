package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Order.class)
public abstract class Order_ extends com.lt.fly.entity.BasicEntity_ {

	public static volatile SingularAttribute<Order, Integer> lotteryResult;
	public static volatile SingularAttribute<Order, Long> issueNumber;
	public static volatile SingularAttribute<Order, String> betsContent;
	public static volatile SingularAttribute<Order, Double> totalMoney;
	public static volatile SingularAttribute<Order, Double> battleResult;
	public static volatile SingularAttribute<Order, String> lotteryType;
	public static volatile SingularAttribute<Order, Integer> betsCount;
	public static volatile SingularAttribute<Order, String> exchangeDetail;
	public static volatile SingularAttribute<Order, BetGroup> betGroup;
	public static volatile SingularAttribute<Order, Double> singleBetting;
	public static volatile SingularAttribute<Order, Integer> resultType;
	public static volatile SingularAttribute<Order, Finance> finance;
	public static volatile SingularAttribute<Order, Integer> status;

}

