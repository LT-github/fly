package com.lt.fly.web.resp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveResp {
	private boolean isSave;

	public SaveResp() {
		super();
	}

	public SaveResp(boolean isSave) {
		super();
		this.isSave = isSave;
	}
}
