package com.lt.fly.web.query;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.QueryBetween;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFind extends DataQueryObjectPage {
    @QueryField(name = "createUser.username",type = QueryType.FULL_LIKE )
    private String username;

    @QueryField(name = "id" , type = QueryType.EQUAL)
    private Long id;

    @QueryField(name = "status", type = QueryType.EQUAL)
    private Integer status;

    @QueryField(name = "resultType", type = QueryType.EQUAL)
    private Integer resultType ;

    @QueryField(name = "createTime",type = QueryType.BEWTEEN )
    private QueryBetween<Long> createTime;

    @QueryField(name = "issueNumber",type = QueryType.EQUAL)
    private Long issueNumber;
}
