package com.lt.fly.web.req;
import com.lt.fly.jpa.support.DataQueryObject;
import com.lt.fly.jpa.support.QueryField;
import com.lt.fly.jpa.support.QueryType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameFind implements DataQueryObject {
    @QueryField(name = "name" , type = QueryType.FULL_LIKE)
    private String name;

    @QueryField(name = "id" , type = QueryType.EQUAL)
    private Long id;

    @QueryField(name = "type" , type = QueryType.FULL_LIKE)
    private String type;
}
