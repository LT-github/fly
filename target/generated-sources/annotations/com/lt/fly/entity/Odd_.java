package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Odd.class)
public abstract class Odd_ extends com.lt.fly.entity.BasicEntity_ {

	public static volatile SetAttribute<Odd, OddGroup> oddGroups;
	public static volatile SingularAttribute<Odd, BetGroup> betGroup;
	public static volatile SingularAttribute<Odd, String> specificOdd;
	public static volatile SingularAttribute<Odd, Double> oddValue;

}

