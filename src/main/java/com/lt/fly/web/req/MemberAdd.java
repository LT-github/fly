package com.lt.fly.web.req;

import com.lt.fly.utils.RegexUtil;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MemberAdd {
	
	@NotNull(message = "用户名不能为空")
	@Pattern(regexp = RegexUtil.CHECK_USER_NAME,message = "用户名必须是6-10位字母、数字、下划线，不能以数字开头")
	private String username;
	@NotNull(message = "密码不能为空")
	@Pattern(regexp = RegexUtil.CHECK_PASSWORD,message = "密码必须是6-20位的字母、数字、下划线")
	private String password;
	@NotNull(message = "状态不能为空")
	private Integer status;
	@NotNull(message = "盘口id不能为空")
	private Long handicapId;

	private String remark;
	private String nickname;
}
