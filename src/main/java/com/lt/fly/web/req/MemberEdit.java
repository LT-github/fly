package com.lt.fly.web.req;

import com.lt.fly.utils.RegexUtil;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
public class MemberEdit {
	
	@NotNull(message = "密码不能为空")
	@Pattern(regexp = RegexUtil.CHECK_PASSWORD,message = "必须是6-20位的字母、数字、下划线")
	private String password;

	private String nickname;

}
