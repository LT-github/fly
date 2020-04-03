package com.lt.fly.web.query;

import lombok.Data;

@Data
public class DetailsFind {

    private String time;

    private Long userId;

    private Integer page = 1;

    private Integer size = 10;
}
