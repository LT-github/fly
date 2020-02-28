package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Admin.class)
public abstract class Admin_ extends com.lt.fly.entity.User_ {

	public static volatile SetAttribute<Admin, Role> roles;
	public static volatile SetAttribute<Admin, Bullent> pushBullents;

}

