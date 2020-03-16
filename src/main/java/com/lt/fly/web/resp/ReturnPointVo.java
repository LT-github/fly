package com.lt.fly.web.resp;

import com.lt.fly.entity.Member;
import lombok.Data;

@Data
public class ReturnPointVo {

    private String username;

    private String nikename;

    private double money;

    private double returnMoney;

}
