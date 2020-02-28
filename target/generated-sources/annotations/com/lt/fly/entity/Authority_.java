package com.lt.fly.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Authority.class)
public abstract class Authority_ extends com.lt.fly.entity.BasicEntity_ {

	public static volatile SingularAttribute<Authority, Authority> parent;
	public static volatile SingularAttribute<Authority, String> identifier;
	public static volatile SetAttribute<Authority, Role> roles;
	public static volatile SingularAttribute<Authority, String> name;
	public static volatile SingularAttribute<Authority, String> description;

}

