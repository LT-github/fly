package com.lt.fly.utils;

import lombok.Getter;

import java.util.regex.Pattern;

public class GlobalConstant {
	
	// SESSION KEY
	public static final String SESSION_TO_USER_INFO = "sessionToUserInfo";
	
	//注单有有效期
	public static final int BETS_ORDER_EFFECTIVE_DATE = 3;
	
	public enum GameStatus {
		
		CLOSE(0, "已关闭"),
		OPEN(1, "已开启");
		
		private int code;

	    private String message;

	    GameStatus(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }

	    public int getCode() {
	        return code;
	    }

	    public void setCode(int code) {
	        this.code = code;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	}
	
	public enum UserType {
		
		SYSTEM(1, "系统用户"),
		ORDINARY(2, "普通用户"),
		MERCHANT(3, "商铺用户");
		
		private int code;

	    private String message;

	    UserType(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }

	    public int getCode() {
	        return code;
	    }

	    public void setCode(int code) {
	        this.code = code;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	}
	
	
	public enum FinanceType {
		
		/*
		 * 1 - 1000 都是支出
		 * 1001 - 2000 都是收入
		 */
		RECHARGE(1, "充值");

	    private int code;

	    private String message;

	    FinanceType(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }

	    public int getCode() {
	        return code;
	    }

	    public void setCode(int code) {
	        this.code = code;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	}
	
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

	    public int getCode() {
	        return code;
	    }

	    public void setCode(int code) {
	        this.code = code;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	}
	
	public enum UserStatus {
		
		PROHIBIT(0, "禁用的用户状态"),
		ACTIVITY(1, "活动的用户状态");

	    private int code;

	    private String message;

	    UserStatus(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }

	    public int getCode() {
	        return code;
	    }

	    public void setCode(int code) {
	        this.code = code;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }
	}
	
	public enum ResultType{
		
		YET(0, "未开奖"),
		HAVE(1, "已开奖");

	    private int code;

	    private String message;

	    ResultType(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }

	    public int getCode() {
	        return code;
	    }

	    public void setCode(int code) {
	        this.code = code;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }
	}
	
	public enum ExchangePrizes{
		
		YET(0, "未兑奖"),
		HAVE(1, "已兑奖");

	    private int code;

	    private String message;

	    ExchangePrizes(int code, String message) {
	        this.code = code;
	        this.message = message;
	    }

	    public int getCode() {
	        return code;
	    }

	    public void setCode(int code) {
	        this.code = code;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }
		
	}
	
	public enum BetsStatus {
		NOTClEARING(0,"未结算"),
		CLEARING(1,"已结算"),
		CANCEL(2,"撤销");
		private int code;

		private String message;

		BetsStatus(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
	
	
	public enum GamePattern {

		P_CHAO(Pattern.compile(".*超.*")),
		P_FAN(Pattern.compile(".*翻.*")),
		P_FU(Pattern.compile(".*复.*")),
		P_MULTI(Pattern.compile(".*;.*"));
		
		
		private Pattern pattern ;

		GamePattern(Pattern pattern) {
			this.pattern = pattern;
		}

		public Pattern getPattern() {
			return pattern;
		}

		public void setPattern(Pattern pattern) {
			this.pattern = pattern;
		}
	}
	
	public enum LotteryResult{
		LOSE(1,"输"),
		WIN(0,"赢");
		
		private int code;

		private String message;

		LotteryResult(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
	
	//下单开关true为开,false为关
	public enum Switch{
		SWITCH(true);

		private boolean flag;

		private Switch(boolean flag) {
			this.flag = flag;
		}

		public boolean isFlag() {
			return flag;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
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

	public enum SettingsKey{
		DEADLINE("deadline");

		private String name;
		private SettingsKey(String name){
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Getter
	public enum GameGroupAddType{
		DEFUALt(1),
		CHANGED(2);
		private Integer code;
		private GameGroupAddType(Integer code){
			this.code = code;
		}

	}

}
