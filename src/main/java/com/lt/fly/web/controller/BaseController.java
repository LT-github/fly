package com.lt.fly.web.controller;

import com.lt.fly.dao.IUserRepository;
import com.lt.fly.entity.User;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.ContextHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Optional;

public class BaseController {
	
	
	@Autowired
	protected IUserRepository iUserRepository;
	
	public void paramsValid(BindingResult bindingResult) throws ClientErrorException {
		if(bindingResult.hasErrors()) {
			List<ObjectError> objErrors = bindingResult.getAllErrors();
			if(null!= objErrors && objErrors.size()>0)
				throw new ClientErrorException(objErrors.get(0).getDefaultMessage());
		}
	}
	
	public User getLoginUser() throws ClientErrorException {
		Long userId = ContextHolderUtil.getTokenUserId();
		Optional<User> optional = iUserRepository.findById(userId);
		if(!optional.isPresent())
			throw new ClientErrorException("标识中的用户不存在");
		
		return optional.get();
	}

	protected  <T> T isNotNull(Optional<T> optional , String msg) throws ClientErrorException {
		if(!optional.isPresent())
			throw new ClientErrorException(msg);
		return optional.get();
	}
}
