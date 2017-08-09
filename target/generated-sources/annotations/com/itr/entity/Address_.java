package com.itr.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Address.class)
public abstract class Address_ {

	public static volatile SingularAttribute<Address, String> zipCode;
	public static volatile SingularAttribute<Address, String> streetAddress;
	public static volatile SingularAttribute<Address, String> city;
	public static volatile SingularAttribute<Address, String> state;

}

