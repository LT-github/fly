package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OddGroup.class)
public abstract class OddGroup_ extends com.lt.fly.entity.BasicEntity_ {

	public static volatile SingularAttribute<OddGroup, Handicap> handicap;
	public static volatile SetAttribute<OddGroup, Odd> odds;
	public static volatile SingularAttribute<OddGroup, String> name;

}

