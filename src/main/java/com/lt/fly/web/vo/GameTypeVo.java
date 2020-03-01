package com.lt.fly.web.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class GameTypeVo {

    private String type;
    private Integer status;

}
