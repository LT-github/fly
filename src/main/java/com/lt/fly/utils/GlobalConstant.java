package com.lt.fly.utils;

import lombok.Getter;
import lombok.Setter;

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

	//封盘开关
	@Getter
	public enum Bet{
		SWITCH(true);

		@Setter
		private boolean flag;

		private Bet(boolean flag) {
			this.flag = flag;
		}
	}

	//最新开奖数据
	@Getter
	public enum NewData{
		ISSUE_NUMBER(1l),
		OPEN_TIME(1l);

		@Setter
		private Long data;

		private NewData(long data) {
			this.data = data;
		}
	}

	//财务类型
	@Getter
	public enum FananceType{
		RECHARGE(1,"充值"),
		BET(2,"投注"),
		CANCLE(3,"撤销"),
		TIMELY_LIUSHUI(4,"实时流水返点"),
		RANGE_LIUSHUI(5,"区间流水返点"),
		RANGE_YINGLI(6,"区间盈利返点");

		private int code;
		private String msg;

		FananceType(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}
	}
}
