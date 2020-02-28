package com.lt.fly.redis.controller;

import com.lt.fly.redis.service.IOrderDTOServiceCache;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.ResultCode;
import com.lt.lxc.pojo.OrderDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/orderCache")
public class OrderCacheController {

	@Resource(name = "redisCache")
	private IOrderDTOServiceCache orderService;
	
	
	/**
	 * 将OrderDTO存入缓存中
	 * @param req
	 * @return
	 */
	@PostMapping("add")
	public HttpResult<Object> save(@RequestBody List<OrderDTO> dto){
		try {
			orderService.add(dto);
			return HttpResult.success("成功");
		} catch (Exception e) {
			return HttpResult.failure(ResultCode.SERVER_ERROR.getCode(),e.getMessage());
		}
	}
	
	/**
	 * 删除orderDTO缓存中的所有数据
	 * @return
	 */
	@DeleteMapping("delete")
	public HttpResult<Object> delete(){
		orderService.remove(null);
		return HttpResult.success("成功");
	}
	
	/**
	 * 查找缓存中的所有数据
	 * @return
	 */
	@GetMapping("find")
	public HttpResult<List<OrderDTO>> findByAll(){
		return HttpResult.success(orderService.findByAll());
	}
}
