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
	public enum AuditStatus {
		
		IN_AUDIT(0, "审核中"),
		AUDIT_PASS(1, "已审核"),
		AUDIT_FAILED(2, "审核未通过");

	    public int code;

	    public String message;

		AuditStatus(int code, String message) {
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
		LOSE(0,"输"),
		WIN(1,"赢");
		
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
		RECHARGE(1,"上分"),
		BET(2,"下注"),
		CANCLE(3,"撤销"),
		TIMELY_LIUSHUI(4,"实时流水"),
		RANGE_LIUSHUI(5,"区间回水"),
		RANGE_YINGLI(6,"区间分红"),
		DESCEND(7,"下分"),
		BET_WIN(8,"下注获胜"),
		RECOMMEND_LIUSHUI(9,"推手区间回水"),
		RECOMMEND_YINGLI(10,"推手区间盈利");


		private int code;
		private String msg;

		FananceType(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}
	}

	//计数类型
	@Getter
	public enum CountType{
		ADD(1,"加"),
		SUBTRACT(2,"减");

		private int code;
		private String msg;

		CountType(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}
	}

	@Getter
	public enum OrderStatus {
		NOTClEARING(0,"未结算"),
		CLEARING(1,"已结算"),
		CANCEL(2,"撤销");
		private int code;

		private String message;

		OrderStatus(int code, String message) {
			this.code = code;
			this.message = message;
		}

	}


	@Getter
	public enum ReturnFind{
		ALL(1,"所有"),
		NOT_HAVE(2,"未结算");
		private int code;
		private String msg;

		ReturnFind(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}
	}

	//会员是否存在盘口中,0:不在, 1:在
	@Getter
	public enum IsHaveHandicap{
		NOT(0,"不在"),
		YSE(1,"在");

		private int code;
		private String msg;

		IsHaveHandicap(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}
	}

	@Getter
	public enum NoMemberHandicap{
		id(000000l);

		private Long code;

		NoMemberHandicap(Long code) {
			this.code = code;
		}
	}
}
