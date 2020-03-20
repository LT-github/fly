package com.lt.fly.web.vo;

import com.lt.fly.entity.Member;
import lombok.Data;

@Data
public class ReturnPointVo {

    private String username;

    private String nikename;

    private double money;

    private double returnMoney;

    private Long memberId;

    private Long time;
}
