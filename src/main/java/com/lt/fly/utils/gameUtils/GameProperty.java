package com.lt.fly.utils.gameUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameProperty {
	
	private Integer count;
	private Double money;
	private Double single;
	
	
	public GameProperty(Integer count, Double money, Double single) {
		super();
		this.count = count;
		this.money = money;
		this.single = single;
	}

}
