package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * 公告
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_bullent")
@Getter
@Setter
public class Bullent extends BasicEntity{

	// 公告的标题
	private String title;
	
	// 公告的内容
	private String content;
	
	// 指定推送的用户集合
	@ManyToMany
    @JoinTable(name = "t_bullent_user",joinColumns = @JoinColumn(name = "bullent_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> pushUsers;
	
	// 开始推送的时间
	@Column
	private Long pushTime;
	
	// 推送截止时间
	@Column
	private Long pushEndTime;
	
	// 公告状态：未推送 0 、已推送 1 、 作废 2
	@Column
	private Integer status;

}
