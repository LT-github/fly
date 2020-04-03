package com.lt.fly.web.vo;

import com.lt.fly.entity.Finance;
import com.lt.fly.entity.Member;
import com.lt.fly.utils.MyBeanUtils;
import lombok.Data;

import java.util.List;

@Data
public class MemberReportVo extends BetReportVo{

    private String username;

    private String nickname;

    private Long id;

    public MemberReportVo(String dateTime, List<Finance> finances, Member member) {
        super(dateTime, finances);
        MyBeanUtils.copyProperties(member,this);
    }
}
