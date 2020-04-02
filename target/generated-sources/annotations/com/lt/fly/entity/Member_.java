package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Member.class)
public abstract class Member_ extends com.lt.fly.entity.User_ {

	public static volatile SingularAttribute<Member, Security> security;
	public static volatile SingularAttribute<Member, Handicap> handicap;
	public static volatile SingularAttribute<Member, String> referralCode;
	public static volatile SingularAttribute<Member, String> remark;
	public static volatile SetAttribute<Member, Proportion> proportions;
	public static volatile SetAttribute<Member, Finance> finances;
	public static volatile SingularAttribute<Member, Integer> type;
	public static volatile SingularAttribute<Member, Integer> isHaveHandicap;

}

