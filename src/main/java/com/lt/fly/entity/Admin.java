package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_admin")
@DiscriminatorValue("Admin")
@Setter
@Getter
public class Admin extends User{


	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "t_role_admin",joinColumns = @JoinColumn(name = "admin_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	// 推送的公告集合
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER , mappedBy = "createUser")
	private Set<Bullent> pushBullents;

}