package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_settings")
@Getter
@Setter
public class Settings extends BasicEntity{

	@Column(name = "data_value")
	private String dataValue;
	@Column(name = "data_key")
	private String dataKey;

}
