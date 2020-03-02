package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@MappedSuperclass
@Setter
@Getter
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

	@ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.DETACH})
	@JoinColumn(name="modify_user_id")
	private User modifyUser;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicEntity other = (BasicEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
