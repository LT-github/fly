package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 数据字典
 * @author Administrator
 *
 */

@Entity
@Table(name = "t_data_dictionary")
@Getter
@Setter
public class DataDictionary extends BasicEntity{
	
//	// 父节点
	@ManyToOne(cascade={CascadeType.MERGE})
	@JoinColumn(name="parent_id")
	private DataDictionary parent;
	

	// 数据值
	@Column(name = "value" , length = 32)
	private String value;
	
	// 顺序
	@Column(name = "sort_no" , length = 32)
	private Integer sortNo;
	
	// 状态 0正常,1删除
	@Column(name = "status" , length = 32)
	private Integer status;
	
	// 描述
	@Column(name = "name" , length = 32)
	private String	name;

}
