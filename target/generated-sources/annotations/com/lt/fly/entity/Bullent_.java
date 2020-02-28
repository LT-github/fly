package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Bullent.class)
public abstract class Bullent_ extends com.lt.fly.entity.BasicEntity_ {

	public static volatile SingularAttribute<Bullent, String> title;
	public static volatile SetAttribute<Bullent, User> pushUsers;
	public static volatile SingularAttribute<Bullent, Long> pushTime;
	public static volatile SingularAttribute<Bullent, Long> pushEndTime;
	public static volatile SingularAttribute<Bullent, String> content;
	public static volatile SingularAttribute<Bullent, Integer> status;

}

