package com.lt.fly.web.req;

import com.lt.fly.utils.RegexUtil;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
public class MemberEditBySystem extends MemberAddByClient{
	

	@NotNull(message = "状态不能为空")
	private Integer status;

	private Long handicapId;

	private String remark;

	@NotNull(message = "会员类型不能为空")
	private Integer type;

}
