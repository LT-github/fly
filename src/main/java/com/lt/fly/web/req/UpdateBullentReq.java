package com.lt.fly.web.req;

import java.util.List;

import javax.validation.constraints.NotNull;

public class UpdateBullentReq {
	
	//公告id
	@NotNull(message = "公告id不能为空")
	private Long id;
	//公告标题
    private String title;
    //公告内容
    private String content;
    //开始推送时间
    
    private Long pushTime;
    // 推送截止时间
   
    private Long endTime;
    // 公告状态：未推送 0 、已推送 1 、 作废 2
    private Integer status;
    
    private List<Long> userIds;
    
   //公告类型
    private Integer type;
    
    
    
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public List<Long> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getPushTime() {
		return pushTime;
	}
	public void setPushTime(Long pushTime) {
		this.pushTime = pushTime;
	}
	
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
    
}
