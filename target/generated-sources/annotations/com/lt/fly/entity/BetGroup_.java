package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BetGroup.class)
public abstract class BetGroup_ extends com.lt.fly.entity.BasicEntity_ {

	public static volatile SingularAttribute<BetGroup, Integer> maximumBet;
	public static volatile SingularAttribute<BetGroup, GameGroup> gameGroup;
	public static volatile SetAttribute<BetGroup, Odd> odds;
	public static volatile SingularAttribute<BetGroup, String> name;
	public static volatile SingularAttribute<BetGroup, Integer> singleBetting;
	public static volatile SingularAttribute<BetGroup, Integer> status;

}

