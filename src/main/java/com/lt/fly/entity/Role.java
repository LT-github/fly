package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "t_role")
@Getter
@Setter
public class Role extends BasicEntity{

	@Column(name = "name" , length = 32)
	private String name;
	
	@Column(name = "description" , length = 255)
	private String description;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_role_authority",joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "authority_id"))
	private Set<Authority> authoritys = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_role_admin",joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "admin_id"))
	private Set<Admin> admins;

}
