package com.lt.fly.jpa;

import com.lt.fly.jpa.support.DataQueryObject;
import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.DataQueryObjectSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


/**
 *
 * @author fewstorng
 **/
@NoRepositoryBean
@Transactional(readOnly=true,rollbackFor = Exception.class)
public interface BaseRepository<T,ID extends Serializable> extends JpaRepository<T,ID>, JpaSpecificationExecutor<T> {

	//	普通查询
	List<T> findAll(DataQueryObject query);

	//	分页查询
	Page<T> findAll(DataQueryObject query, Pageable page);

	//	分页查询
	Page<T> findAll(DataQueryObjectPage dataQueryObjectpage);

	//	排序查询
	List<T> findAll(DataQueryObject dataQueryObject, Sort sort);

	//	排序查询
	List<T> findAll(DataQueryObjectSort dataQueryObjectSort);
	
}
