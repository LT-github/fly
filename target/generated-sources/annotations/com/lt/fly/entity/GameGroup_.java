package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(GameGroup.class)
public abstract class GameGroup_ extends com.lt.fly.entity.BasicEntity_ {

	public static volatile SingularAttribute<GameGroup, Integer> addType;
	public static volatile SingularAttribute<GameGroup, String> name;
	public static volatile SingularAttribute<GameGroup, String> description;
	public static volatile SetAttribute<GameGroup, BetGroup> betGroups;
	public static volatile SingularAttribute<GameGroup, String> type;
	public static volatile SingularAttribute<GameGroup, String> pingYinName;
	public static volatile SingularAttribute<GameGroup, Integer> status;

}

