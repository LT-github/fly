package com.lt.fly.dao;


import com.lt.fly.entity.DataDictionary;
import com.lt.fly.jpa.BaseRepository;

import java.util.List;

public interface IDataDictionaryRepository extends BaseRepository<DataDictionary, Long> {

	public List<DataDictionary> findByParentId(Long parentId);
	
	public DataDictionary findByValue(String value);
	
	
}
