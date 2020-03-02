package com.lt.fly.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.lt.fly.entity.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BullentVo {

	   // 公告的标题
		private String title;
		
		// 公告的内容
		private String content;
		
		// 公告的作者的id
		 
		private Long  authorId;	
		
		//公告作者的用户名
		private String authorUsername;
								
		// 开始推送的时间
		private Long pushTime;		
		// 推送截止时间
		private Long pushEndTime;
		
		// 公告状态：未推送 0 、已推送 1 、 作废 2	
		private Integer status;
		//公告id
		private Long id;
		//创建时间
		private Long createTime;
		//
		private List<Long> time=new ArrayList<Long>();
		
		//操作时间
		private Long modifyTime;

		
		

	
}
