package com.lt.fly.web.query;


import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetGroupFind extends DataQueryObjectPage {

    @QueryField(name = "name" , type = QueryType.FULL_LIKE)
    private String name;

    @QueryField(name = "id" , type = QueryType.EQUAL)
    private Long id;

    @QueryField(name = "gameGroup.type" , type = QueryType.FULL_LIKE)
    private String type;

    private Long oddGroupId;
}
