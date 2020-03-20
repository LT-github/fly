package com.lt.fly.utils.gameUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameProperty {
	
	private int count;
	private double money;
	private double single;
	
	
	public GameProperty(int count, double money, double single) {
		super();
		this.count = count;
		this.money = money;
		this.single = single;
	}

}
