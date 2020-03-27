package com.lt.fly.web.query;

import com.lt.fly.jpa.support.DataQueryObject;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReturnPointFindPage extends MemberFind {

    private Integer type;

    private Integer findType ;

    protected Integer page = 0;

    protected Integer size = 10;
}
