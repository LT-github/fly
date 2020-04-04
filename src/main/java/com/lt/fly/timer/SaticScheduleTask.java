package com.lt.fly.timer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.lt.fly.Service.IFinanceService;
import com.lt.fly.dao.IHandicapRepository;
import com.lt.fly.entity.Handicap;
import com.lt.fly.exception.ClientErrorException;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
	
	@Autowired
	private IFinanceService iFinanceService;
	@Autowired
	private IHandicapRepository handicapRepository;
    //3.添加定时任务
    //@Scheduled(cron = "0/5 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
//    private void configureTasks() throws ClientErrorException {
//    	
//    	iFinanceService.addTime(14, null, null, null,1);
//    }
	
}
