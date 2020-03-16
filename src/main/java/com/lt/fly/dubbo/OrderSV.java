package com.lt.fly.dubbo;

import java.util.Map;

import com.lt.fly.Service.IOrderService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.lt.lxc.pojo.OrderDTO;
import com.lt.lxc.service.OrderISV;

@Service(version = "1.0.0")
public class OrderSV implements OrderISV{
	
	
	
	@Autowired
	private IOrderService iOrderService;
	
	@Override
	public void orderSettle(Map<Long,OrderDTO> map)  {
		iOrderService.settle(map);

		System.err.println(">>>>> 结算成功");
	}

}
