package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;




@Setter
@Getter
@MappedSuperclass
public class BasicEntity {
	
	@Id
	private Long id;
	
	@Column
	private Long createTime;

	@Column
	private Long modifyTime;

	@ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.DETACH})
	@JoinColumn(name="create_user_id")
	private User createUser;

	@ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.DETACH})
	@JoinColumn(name="modify_user_id")
	private User modifyUser;

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
