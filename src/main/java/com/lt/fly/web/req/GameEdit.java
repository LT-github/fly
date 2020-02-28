package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameEdit {
    private String type;
    private String pingYinName;
    private Integer status;
    private String description;
}
