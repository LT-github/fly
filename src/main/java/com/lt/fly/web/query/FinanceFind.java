package com.lt.fly.web.query;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.QueryBetween;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FinanceFind extends DataQueryObjectPage {

    @QueryField(type = QueryType.EQUAL , name="type")
    private Integer type;

    @QueryField(type = QueryType.EQUAL , name="auditStatus")
    private Integer auditStatus;

    @QueryField(type = QueryType.BEWTEEN , name="createTime")
    private QueryBetween<Long> createTime;

    @QueryField(type = QueryType.FULL_LIKE , name="createUser.username")
    private String username;

    @QueryField(type = QueryType.EQUAL , name="createUser.id")
    private Long userId;


}
