package com.lt.fly.web.vo;



import java.util.ArrayList;
import java.util.List;

import com.lt.fly.entity.Finance;
import com.lt.fly.entity.User;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AuditFindVo {

	    // 该条财务记录的状态  0 正常 、1 禁用
		private Integer status;		
		// 该条财务记录的金额
		private Double money;		
		// 这笔财务的描述
		private String description;		
		//用户当时余额
		private Double balance;
		// 财务的审核状态  0 审核中 1审核通过 2审核失败
		private Integer auditStatus;
		// 审核的类型  0 需要审核 1不需要审核
		private Integer auditType;
		//计数类型(1加 2减)
		private Integer countType;
		//财务类型(1:充值,2:返点,3:投注)
		private Integer type;
		
		private Long id;
		
		private Long createTime;

		private Long modifyTime;

		private User createUser;

		private User modifyUser;

		public AuditFindVo() {
			
		}
		
		public AuditFindVo(Finance finance) {
  
			this.id=finance.getId();
			this.createTime=finance.getCreateTime();
			this.modifyTime=finance.getModifyTime();
			this.createUser=finance.getCreateUser();
			this.modifyUser=finance.getModifyUser();
			this.status=finance.getStatus();
			this.money=finance.getMoney();
			this.description=finance.getDescription();
			this.balance=finance.getBalance();
			this.auditStatus=finance.getAuditStatus();
			this.auditType=finance.getAuditType();
			this.type=finance.getType();
			this.countType=finance.getCountType();
			
			
			
		}
		
		public static List<AuditFindVo> toVo(List<Finance> financeList){
			
			List<AuditFindVo> resp = new ArrayList<AuditFindVo>();
			for (Finance item : financeList) {
				resp.add(new AuditFindVo(item));
			}
			return resp;
		}

		public AuditFindVo(Integer status, Double money, String description, Double balance, Integer auditStatus,
				Integer auditType, Integer countType, Integer type, Long id, Long createTime, Long modifyTime,
				User createUser, User modifyUser) {
			super();
			this.status = status;
			this.money = money;
			this.description = description;
			this.balance = balance;
			this.auditStatus = auditStatus;
			this.auditType = auditType;
			this.countType = countType;
			this.type = type;
			this.id = id;
			this.createTime = createTime;
			this.modifyTime = modifyTime;
			this.createUser = createUser;
			this.modifyUser = modifyUser;
		}
}
