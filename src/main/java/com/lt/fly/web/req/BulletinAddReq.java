package com.lt.fly.web.req;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 添加公告,给所有人
 * @author Administrator
 *
 */
public class BulletinAddReq {
	
	@NotNull(message = "公告标题不能为空")
	private String title;
	@NotNull(message = "公告内容不能为空")
	private String content;			
	//公告状态
	@NotNull(message = "请设置公告状态")
	private Integer status;
	//公告推送时间前
	private Long pushTime;
	//公告推送时间后
	private Long endTime;
	
    private List<Long> userIds;
	
	private Long id;
	
	//公告类型	
	private Integer type;
	
	
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	
	
	
	

}
