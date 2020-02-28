package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "t_authority")
@Setter
@Getter
public class Authority extends BasicEntity{
	
	// 该权限所属的父权限对象
	@OneToOne
	@JoinColumn(name="parent_id",referencedColumnName="id")
	private Authority parent;
	
	// 该权限的名称
	@Column(name = "name" , length = 32)
	private String name;
	
	// 该权限的描述
	@Column(name = "description" , length = 255)
	private String description;
	
	// 该权限的标识符
	@Column(name = "identifier" , length = 50)
	private String identifier;

	// 该权限关联的角色
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "t_role_authority",joinColumns = @JoinColumn(name = "authority_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

}
