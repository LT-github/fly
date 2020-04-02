package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Finance.class)
public abstract class Finance_ extends com.lt.fly.entity.BasicEntity_ {

	public static volatile SingularAttribute<Finance, Double> money;
	public static volatile SingularAttribute<Finance, Double> balance;
	public static volatile SingularAttribute<Finance, String> description;
	public static volatile SingularAttribute<Finance, Integer> auditStatus;
	public static volatile SingularAttribute<Finance, Integer> countType;
	public static volatile SingularAttribute<Finance, Integer> type;

}

