package com.lt.fly.utils;

import lombok.Getter;

import java.util.regex.Pattern;

public class GlobalConstant {
	
	// SESSION KEY
	public static final String SESSION_TO_USER_INFO = "sessionToUserInfo";
	
	//注单有有效期
	public static final int BETS_ORDER_EFFECTIVE_DATE = 3;

	//玩法状态
	@Getter
	public enum GameStatus {
		
		CLOSE(0, "关闭"),
		OPEN(1, "开启");
		
		private int code;

	    private String message;

	    GameStatus(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }

	}
	
	//充值财务状态
	@Getter
	public enum FinanceStatus {
		
		IN_AUDIT(1, "审核中"),
		AUDIT_PASS(2, "已审核"),
		AUDIT_FAILED(3, "审核未通过");

	    public int code;

	    public String message;

	    FinanceStatus(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }
	}

	//用户状态
	@Getter
	public enum UserStatus {
		
		PROHIBIT(0, "禁用"),
		ACTIVITY(1, "活动");

	    private int code;

	    private String message;

	    UserStatus(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }

	}

	//注单开奖结果
	@Getter
	public enum LotteryResult{
		LOSE(1,"输"),
		WIN(0,"赢");
		
		private int code;

		private String message;

		LotteryResult(int code, String message) {
			this.code = code;
			this.message = message;
		}
	}
	
	/**
	 * 最新的开奖数据
	 * @author Administrator
	 *
	 */
	public enum NewData{
		ISSUE_NUMBER(1l),
		OPEN_TIME(1l);

		private Long data;

		private NewData(long data) {
			this.data = data;
		}
		public Long getData() {
			return data;
		}
		public void setData(Long data) {
			this.data = data;
		}
		
		
	}
}
