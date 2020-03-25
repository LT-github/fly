package com.lt.fly.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.lt.fly.entity.Member;
import com.lt.fly.utils.GlobalConstant;
import jdk.nashorn.internal.objects.Global;
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
	
	// 用户手机号
	private String mobile;

	// 用户IP
	private String ip;
	
	// 用户状态
	private Integer status;
	
	// 最后登录时间
	private Long lastLoginTime;
	
	// 备注
	private String remark;

	// 盘口Id
	private Long HandicapId;

	//盘口名称
	private String HandicapName;

	//会员类型
	private Integer type;

	//推荐人
	private String referrerName;

	//邀请码
	private String referralCode;

	private List<ProportionVo> proportionVos;
	
	
	public MemberVo() {
	}
	public MemberVo(Member obj) {
		BeanUtils.copyProperties(obj, this);
		if (null != obj.getHandicap()) {
			this.HandicapId = obj.getHandicap().getId();
			this.HandicapName = obj.getHandicap().getName();
		}
		if (null != obj.getModifyUser()){
			this.referrerName = obj.getModifyUser().getUsername();
		}
		if (type.equals(GlobalConstant.MemberType.REFERRER.getCode()) && null != obj.getProportions() && 0 !=obj.getProportions().size()){
			this.setProportionVos(ProportionVo.toVo(new ArrayList<>(obj.getProportions())));
		}
	}
	
	public static List<MemberVo> toVo(List<Member> list){
		List<MemberVo> res = new ArrayList<MemberVo>();
		for(Member item : list) {
			res.add(new MemberVo(item));
		}
		return res;
	}

}
