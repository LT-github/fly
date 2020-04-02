package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Proportion.class)
public abstract class Proportion_ extends com.lt.fly.entity.BasicEntity_ {

	public static volatile SingularAttribute<Proportion, DataDictionary> returnPoint;
	public static volatile SingularAttribute<Proportion, String> ranges;
	public static volatile SetAttribute<Proportion, Member> members;
	public static volatile SingularAttribute<Proportion, String> description;
	public static volatile SetAttribute<Proportion, Handicap> handicaps;
	public static volatile SingularAttribute<Proportion, Double> proportionVal;
	public static volatile SingularAttribute<Proportion, String> discriminator;

}

