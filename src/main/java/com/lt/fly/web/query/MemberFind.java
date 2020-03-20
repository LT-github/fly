package com.lt.fly.web.query;

import com.lt.fly.jpa.support.DataQueryObject;
import com.lt.fly.jpa.support.QueryBetween;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;
import lombok.Data;

@Data
public class MemberFind implements DataQueryObject {

    @QueryField(name = "status" , type = QueryType.EQUAL)
    private Integer status;

    @QueryField(name = "handicap.id",type = QueryType.EQUAL)
    private Long handicapId;

    @QueryField(name = "username",type = QueryType.FULL_LIKE)
    private String username;

    @QueryField(name = "nickname",type = QueryType.FULL_LIKE)
    private String nickname;

    @QueryField(name = "createTime",type = QueryType.BEWTEEN)
    private QueryBetween<Long> createTime;

    @QueryField(name = "isHaveHandicap",type =QueryType.EQUAL)
    private Integer isHaveHandicap;
}
