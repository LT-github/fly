package com.lt.fly.web.req;


import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;

public class GameContentFind extends DataQueryObjectPage {

    @QueryField(name = "name" , type = QueryType.FULL_LIKE)
    private String name;

    @QueryField(name = "id" , type = QueryType.EQUAL)
    private Long id;

    @QueryField(name = "game.type" , type = QueryType.FULL_LIKE)
    private String type;
}
