package com.lt.fly.web.vo;

import com.lt.fly.entity.Proportion;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProportionVo{
	private Long id;
	private Double proportionVal;
	private DataDictionaryVo returnPointType;
	private String description;
	private String rangeMoney;
	public ProportionVo() {}
	
	public ProportionVo(Proportion proportion) {
		this.id = proportion.getId();
		this.description = proportion.getDescription();
		this.proportionVal = proportion.getProportionVal();
		this.returnPointType = new DataDictionaryVo(proportion.getReturnPoint());
		if(null != proportion.getRanges()) {
			this.rangeMoney = proportion.getRanges();
		}
		
	}
	
	public static List<ProportionVo> toVo(List<Proportion> list) {
		List<ProportionVo> res = new ArrayList<ProportionVo>();
		for(Proportion item:list) {
			res.add(new ProportionVo(item));
		}
		return res;
	}
	
}
