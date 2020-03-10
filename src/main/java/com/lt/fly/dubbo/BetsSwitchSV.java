package com.lt.fly.dubbo;


import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import com.lt.fly.redis.service.IOrderDTOServiceCache;
import com.lt.fly.utils.GlobalConstant;
import org.apache.dubbo.config.annotation.Service;

import com.lt.lxc.pojo.OrderDTO;
import com.lt.lxc.service.BetsSwitchISV;

@Service(version = "1.0.0")
public class BetsSwitchSV implements BetsSwitchISV{

	@Resource(name = "redisCache")
	private IOrderDTOServiceCache iOrderServiceCache;
	
	@Override
	public void openBets(Long issueNumber) {
		System.err.println(""+LocalDateTime.now()+">>>>> 开始下单"+"当前期号为"+issueNumber);

		//清空OrderDTO的缓存
		iOrderServiceCache.remove(null);
		//存入期号
		GlobalConstant.NewData.ISSUE_NUMBER.setData(issueNumber);
		//改变状态
		GlobalConstant.Bet.SWITCH.setFlag(true);
	}

	@Override
	public List<OrderDTO> closeBets() {
		System.err.println(""+LocalDateTime.now()+">>>>> 停止下单,等待开奖.......");
		
		GlobalConstant.Bet.SWITCH.setFlag(false);
		return iOrderServiceCache.findByAll();
	}

}
