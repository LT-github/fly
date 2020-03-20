package com.lt.fly.web.req;

import com.lt.fly.utils.RegexUtil;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class MemberEditByClient {

    @NotNull(message = "密码不能为空")
    @Pattern(regexp = RegexUtil.CHECK_PASSWORD,message = "必须是6-20位的字母、数字、下划线")
    private String password;

    private String nickname;

}
