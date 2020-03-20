package com.lt.fly.web.req;

import com.lt.fly.utils.RegexUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class MemberAddByClient {

    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = RegexUtil.CHECK_USER_NAME,message = "用户名必须是6-10位字母、数字、下划线，不能以数字开头")
    private String username;
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = RegexUtil.CHECK_PASSWORD,message = "密码必须是6-20位的字母、数字、下划线")
    private String password;
    @NotNull(message = "电话号码不能为空")
    private String mobile;

    private String nickname;

    //推荐码
    private String referralCode;
}
