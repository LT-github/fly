package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ extends com.lt.fly.entity.BasicEntity_ {

	public static volatile SingularAttribute<User, Long> lastLoginTime;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, String> ip;
	public static volatile SingularAttribute<User, String> nickname;
	public static volatile SingularAttribute<User, String> mobile;
	public static volatile SetAttribute<User, Bullent> receiveBullents;
	public static volatile SingularAttribute<User, String> username;
	public static volatile SingularAttribute<User, Integer> status;
	public static volatile SingularAttribute<User, String> discriminator;

}

