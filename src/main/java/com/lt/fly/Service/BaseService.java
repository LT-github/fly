package com.lt.fly.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.lt.fly.dao.IUserRepository;
import com.lt.fly.entity.User;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.ContextHolderUtil;
import com.lt.fly.utils.IdWorker;



public class BaseService {

	@Autowired
	protected IUserRepository iUserRepository;
	
	@Autowired
	protected IdWorker idWorker;
	
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
