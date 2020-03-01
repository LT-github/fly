package com.lt.fly.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.lt.fly.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.Store;
import org.springframework.beans.BeanUtils;


@Getter
@Setter
public class MemberVo {
	
	private Long id;
	
	// 用户名
	private String username;
	
	// 密码
	private String password;
	
	// 昵称
	private String nickname;
	
	// 用户真实姓名
	private String realname;
	
	// 身份证号
	private String idcard;
	
	// 用户手机号
	private String mobile;

	// 用户IP
	private String ip;
	
	// 用户状态
	private Integer status;
	
	// 最后登录时间
	private Long lastLoginTime;
	
	// 余额
	private Double balance;
	
	// 备注
	private String remark;
	
	// 盘口Id
	private Long HandicapId;
	
	//盘口名称
	private String HandicapName;
	
	
	public MemberVo() {
	}
	public MemberVo(Member obj) {
		BeanUtils.copyProperties(obj, this);
		this.HandicapId = obj.getHandicap().getId();
		this.HandicapName = obj.getHandicap().getName();
	}
	
	public static List<MemberVo> toVo(List<Member> list){
		List<MemberVo> res = new ArrayList<MemberVo>();
		for(Member item : list) {
			res.add(new MemberVo(item));
		}
		return res;
	}

}
