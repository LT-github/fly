package com.lt.fly.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.lt.fly.entity.Role;
import com.lt.fly.entity.SettleTime;

import lombok.Data;

@Data
public class SettleTimeVo {

	
	private Long id;


	private Long createTime;


	private Long modifyTime;


	private Long createUserId;


	private Long modifyUserId;

	private Long  settleTimeAll;
	//统一结算的状态（1选中 0未选中）
	private Integer  status;
	public SettleTimeVo() {}
	public SettleTimeVo(SettleTime vo) {
		super();
		this.id = vo.getId();
		if(vo.getCreateTime()!=null)
		this.createTime =vo.getCreateTime();
		if(vo.getModifyTime()!=null)
		this.modifyTime = vo.getModifyTime();
		if(vo.getCreateUser()!=null)
		this.createUserId = vo.getCreateUser().getId();
		if(vo.getModifyUser()!=null)
		this.modifyUserId = vo.getModifyUser().getId();
		if(vo.getSettleTimeAll()!=null)
		this.settleTimeAll = vo.getSettleTimeAll();
		if(vo.getStatus()!=null)
		this.status = vo.getStatus();
	}
	public static List<SettleTimeVo> toVo(List<SettleTime> list){
		List<SettleTimeVo> resp = new ArrayList<SettleTimeVo>();
		for(SettleTime item : list) {
			resp.add(new SettleTimeVo(item));
		}
		return resp;
	}
}
