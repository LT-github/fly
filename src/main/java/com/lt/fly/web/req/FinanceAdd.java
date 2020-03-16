package com.lt.fly.web.req;

import javax.validation.constraints.NotNull;

import com.lt.fly.entity.Order;

import com.lt.fly.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

@Getter
@Setter
public class FinanceAdd {
	//金额
	@NotNull(message = "金额不能为空")
	private double money;
}
