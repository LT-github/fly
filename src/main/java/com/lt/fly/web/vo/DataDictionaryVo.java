package com.lt.fly.web.vo;

import com.lt.fly.entity.DataDictionary;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
public class DataDictionaryVo {
	
	private Long parentId;
	private String parentName;
	private Long id;
	private String value;
	private Integer sortNo;
	private Integer status;
	private String	name;
	private List<DataDictionaryVo> childVos;

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

	public DataDictionaryVo(DataDictionary entity,List<DataDictionary> child) {
		this(entity);
		if (null != child && 0 != child.size()){
			this.childVos = this.toVo(child);
		}
	}


	public static List<DataDictionaryVo> toVo(List<DataDictionary> list){
		
		List<DataDictionaryVo> response = new ArrayList<DataDictionaryVo>();
		for(DataDictionary item : list) {
			response.add(new DataDictionaryVo(item));
		}
		return response;
	}


}
