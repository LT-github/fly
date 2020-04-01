package com.lt.fly.web.query;

import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;
import lombok.Data;

@Data
public class MemberReportFind extends BetReportFind{

    @QueryField(name = "username" , type = QueryType.FULL_LIKE)
    private String username;
    @QueryField(name = "nickname" , type = QueryType.FULL_LIKE)
    private String nickname;
}
