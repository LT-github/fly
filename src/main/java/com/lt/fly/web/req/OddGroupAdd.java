package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OddGroupAdd {

    private String name;

    private List<Long> oddIds;

}
