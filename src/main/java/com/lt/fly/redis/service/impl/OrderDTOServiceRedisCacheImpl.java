package com.lt.fly.redis.service.impl;

import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.redis.service.IOrderDTOServiceCache;
import com.lt.lxc.pojo.OrderDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("redisCache")
public class OrderDTOServiceRedisCacheImpl implements IOrderDTOServiceCache {

	public static final String KEYNAME = "order:list";
	
	@Resource
	private RedisTemplate<String, List<OrderDTO>> redisTemplate;  
	
	@Override
	public void add(List<OrderDTO> obj) throws ClientErrorException {
		List<OrderDTO> list = redisTemplate.hasKey(KEYNAME)?redisTemplate.opsForValue().get(KEYNAME):new ArrayList<OrderDTO>();
		list.addAll(obj);
		redisTemplate.opsForValue().set(KEYNAME, list);
	}

	@Override
	public void remove(Long orderId) {
		if(null == orderId) {
			redisTemplate.delete(KEYNAME);
		}else {
			List<OrderDTO> dtos = redisTemplate.opsForValue().get(KEYNAME);
			for (OrderDTO orderDTO : dtos) {
				if(orderDTO.getId() == orderId) {
					dtos.remove(orderDTO);
				}
			}
		}
	}

	@Override
	public List<OrderDTO> findByAll() {
		return  redisTemplate.opsForValue().get(KEYNAME);
	}

}
