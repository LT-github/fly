package com.lt.fly.web.vo;

import com.lt.fly.entity.DataDictionary;

import java.util.ArrayList;
import java.util.List;


public class DataDictionaryVo {
	
	private Long parentId;
	private String parentName;
	private Long id;
	private String value;
	private Integer sortNo;
	private Integer status;
	private String	name;
	public DataDictionaryVo() {
	}
	
	public DataDictionaryVo(DataDictionary entity) {
		
		if(entity.getParent()!=null) {
			this.parentId = entity.getParent().getId();
			this.parentName = entity.getParent().getName();
		}
		this.id = entity.getId();
		this.name = entity.getName();
		this.sortNo = entity.getSortNo();
		this.status = entity.getStatus();
		this.value = entity.getValue();
	}
	
	public static List<DataDictionaryVo> toVo(List<DataDictionary> list){
		
		List<DataDictionaryVo> response = new ArrayList<DataDictionaryVo>();
		for(DataDictionary item : list) {
			response.add(new DataDictionaryVo(item));
		}
		return response;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
