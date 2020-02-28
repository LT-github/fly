package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Role.class)
public abstract class Role_ extends com.lt.fly.entity.BasicEntity_ {

	public static volatile SingularAttribute<Role, String> name;
	public static volatile SingularAttribute<Role, String> description;
	public static volatile SetAttribute<Role, Authority> authoritys;
	public static volatile SetAttribute<Role, Admin> admins;

}

