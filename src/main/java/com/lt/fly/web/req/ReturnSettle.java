package com.lt.fly.web.req;

import lombok.Data;

@Data
public class ReturnSettle {
    private Integer type;
    private Double money;
    private String description;
}
