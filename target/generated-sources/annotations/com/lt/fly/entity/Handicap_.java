package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Handicap.class)
public abstract class Handicap_ extends com.lt.fly.entity.BasicEntity_ {

	public static volatile SingularAttribute<Handicap, OddGroup> oddGroup;
	public static volatile SingularAttribute<Handicap, DataDictionary> liushui;
	public static volatile SetAttribute<Handicap, Member> members;
	public static volatile SingularAttribute<Handicap, String> name;
	public static volatile SetAttribute<Handicap, Proportion> proportions;
	public static volatile SingularAttribute<Handicap, DataDictionary> yingkui;
	public static volatile SingularAttribute<Handicap, String> discriminator;

}

