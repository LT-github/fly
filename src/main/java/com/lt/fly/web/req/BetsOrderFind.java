package com.lt.fly.web.req;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.QueryBetween;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;

public class BetsOrderFind extends DataQueryObjectPage {
    @QueryField(name = "ticket.bettingUser.username",type = QueryType.FULL_LIKE )
    private String username;

    @QueryField(name = "ticket.id" , type = QueryType.EQUAL)
    private Long id;

    @QueryField(name = "ticket.status", type = QueryType.EQUAL)
    private Integer status;

    @QueryField(name = "ticket.resultType", type = QueryType.EQUAL)
    private Integer resultType ;

    @QueryField(name = "ticket.exchangePrizes", type = QueryType.EQUAL)
    private Integer exchangePrizes ;

    @QueryField(name = "createTime",type = QueryType.BEWTEEN )
    private QueryBetween<Long> createTime;
}
