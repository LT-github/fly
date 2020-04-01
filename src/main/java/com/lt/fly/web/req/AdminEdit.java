package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class AdminEdit {
//	@NotNull(message = "密码不能为空")
//	private String password;
	
	@NotNull(message = "昵称不能为空")
	private String nickname;

	@NotNull(message = "电话号码不能为空")
	private String mobile;
	
	@NotNull(message = "用户状态不能为空")
	private Integer status;
	
	private List<Long> roleIds;

}
