package com.lt.fly.redis.service;

import com.lt.fly.exception.ClientErrorException;
import com.lt.lxc.pojo.OrderDTO;

import java.util.List;


public interface IOrderDTOServiceCache {

	public void add(List<OrderDTO> dto)throws ClientErrorException;
	public void remove(Long orderId);
	public List<OrderDTO> findByAll();
}
