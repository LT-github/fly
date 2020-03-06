package com.lt.fly.web.controller;

import java.util.List;
import java.util.Optional;

import com.lt.fly.annotation.RequiredPermission;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IDataDictionaryRepository;
import com.lt.fly.entity.DataDictionary;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.utils.MyBeanUtils;
import com.lt.fly.web.req.DataDictionaryAdd;
import com.lt.fly.web.query.DataDictionaryFind;
import com.lt.fly.web.vo.DataDictionaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data-dictionary")
public class DataDictionaryController extends BaseController{
	
	@Autowired
	private IDataDictionaryRepository iDataDictionaryRepository;
	
	@Autowired
	protected IdWorker idWorker;
	
	@RequiredPermission(value="findDataDictionaryById")
	@GetMapping("/{id}")
	@UserLoginToken
	public HttpResult<DataDictionaryVo> findById(@PathVariable Long id) {
		
		Optional<DataDictionary> optional = iDataDictionaryRepository.findById(id);
		if(!optional.isPresent())
			throw new RuntimeException("该code的实体不存在");
		
		return HttpResult.success(new DataDictionaryVo(optional.get()),"查询成功");
	}
	
	@RequiredPermission(value="findDataDictionary")
	@GetMapping("/all")
	@UserLoginToken
	public HttpResult<List<DataDictionaryVo>> findAll(DataDictionaryFind query){
		
		List<DataDictionary> list = iDataDictionaryRepository.findAll(query);
		return HttpResult.success(DataDictionaryVo.toVo(list), "查询成功");
	}
	
	@PostMapping
	@UserLoginToken
	public HttpResult<DataDictionaryVo> addDataDictionary(@RequestBody DataDictionaryAdd obj , BindingResult bindingResult)
			throws ClientErrorException {
		
		this.paramsValid(bindingResult);
		
		Long parentId = obj.getParentId();
		
		DataDictionary objSave = new DataDictionary();
		objSave.setId(idWorker.nextId());
		objSave.setCreateTime(System.currentTimeMillis());
		objSave.setName(obj.getName());
		objSave.setValue(obj.getValue());
		objSave.setStatus(obj.getStatus()==null?0:obj.getStatus());
		
		if(parentId != null) {
			Optional<DataDictionary> optional = iDataDictionaryRepository.findById(parentId);
			if(!optional.isPresent()) {
				throw new ClientErrorException("传递的父元素ID查询不到实体");
			}
			objSave.setParent(optional.get());
			DataDictionaryFind query = new DataDictionaryFind();
			query.setParentId(parentId);
			List<DataDictionary> list = iDataDictionaryRepository.findAll(query);
			if(null != list) {
				objSave.setSortNo(list.size() + 1);
			}else {
				objSave.setSortNo(1);
			}
		}else {
			objSave.setSortNo(0);
		}
		
		iDataDictionaryRepository.save(objSave);
		
		return HttpResult.success(new DataDictionaryVo(objSave),objSave.getName() + "添加成功");
	}
	
	@RequiredPermission(value="deleteChild")
	@DeleteMapping("/{id}")
	@UserLoginToken
	public HttpResult<DataDictionaryVo> deleteChild(@PathVariable Long id) 
			throws ClientErrorException {
		Optional<DataDictionary> optional = iDataDictionaryRepository.findById(id);
		if(!optional.isPresent()) {
			throw new ClientErrorException("数据库id不存在");
		}
		DataDictionary objDelete = optional.get();
		
		iDataDictionaryRepository.deleteById(objDelete.getId());
		
		return HttpResult.success(new DataDictionaryVo(objDelete),objDelete.getName() + "删除成功");
	}
	@RequiredPermission(value="updateDataDictionary")
	@PutMapping("/{id}")
	@UserLoginToken
	public HttpResult<DataDictionaryVo> updateDataDictionary(@PathVariable Long id ,@RequestBody DataDictionaryAdd obj, 
			BindingResult bindingResult) throws ClientErrorException{
		this.paramsValid(bindingResult);
		
		Optional<DataDictionary> optional = iDataDictionaryRepository.findById(id);
		if(!optional.isPresent()) {
			throw new ClientErrorException("数据库id不存在");
		}
		DataDictionary objEditor = optional.get();
		
		MyBeanUtils.copyProperties(obj, objEditor);
		
		if(obj.getParentId() != null) {
			Optional<DataDictionary> findById = iDataDictionaryRepository.findById(obj.getParentId());
			if(!findById.isPresent()) {
				throw new ClientErrorException("parentId 不存在");
			}
			DataDictionary parent = findById.get();
			objEditor.setParent(parent);
		}
		
		iDataDictionaryRepository.save(objEditor);
		
		return HttpResult.success(new DataDictionaryVo(objEditor),objEditor.getName() + "修改成功");
	}
}
