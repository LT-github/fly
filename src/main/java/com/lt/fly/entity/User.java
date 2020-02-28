package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "t_user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator")
@DiscriminatorValue("User")
@Getter
@Setter
public class User extends BasicEntity{
	
	// 用户名
	@Column(name = "username",length = 32)
	private String username;
	
	// 密码
	@Column(name = "password",length = 32)
	private String password;
	
	// 昵称
	@Column(name = "nickname",length = 32)
	private String nickname;
	
	// 用户真实姓名
	@Column(name = "realname",length = 32)
	private String realname;
	
	// 身份证号
	@Column(name = "idcard",length = 32)
	private String idcard;
	
	// 用户手机号
	@Column(name = "mobile",length = 32)
	private String mobile;

	// 用户IP
	@Column(name = "ip_add",length = 32)
	private String ip;
	
	// 用户状态
	@Column(name = "status",length = 32)
	private Integer status;
	
	// 最后登录时间
	@Column(name = "last_login_time")
	private Long lastLoginTime;
	
	// 鉴别器
	@Column(name="discriminator", insertable = false, updatable = false)
	private String discriminator;
	
	// 接收的公告
	@ManyToMany
    @JoinTable(name = "t_bullent_user",joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "bullent_id"))
	private Set<Bullent> receiveBullents;
	
}
