package com.lt.fly.web.query;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;
import lombok.Data;

@Data
public class ProportionFind extends DataQueryObjectPage {

    @QueryField(name = "returnPoint.parent.id",type = QueryType.EQUAL)
    private Long parentId;

}
