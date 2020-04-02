package com.lt.fly.web.log;

import com.lt.fly.utils.GlobalConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @author Promise
* @createTime 2018年12月18日 下午9:26:25
* @description  定义一个方法级别的@log注解
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
	String value() default "";

	/**
	 * 日志类型
	 *
	 * @return
	 */
	GlobalConstant.LogType type() default GlobalConstant.LogType.OPERATION;
}
