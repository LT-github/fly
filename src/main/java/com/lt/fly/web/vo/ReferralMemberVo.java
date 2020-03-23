package com.lt.fly.web.vo;

import lombok.Data;

@Data
public class ReferralMemberVo {

    //推荐人用户名
    private String referralName;

    //推手用户名
    private String MemberName;

    //推手昵称
    private String nickName;

    //流水总额(所推荐的所有会员)
    private Double waterTotal = 0.0;

    //盈亏总额(所推荐的所有会员)
    private Double betResultTotal = 0.0;

    //推荐人数
    private Integer referralNumber = 0;

    //分红总额
    private Double dividendTotal = 0.0;

    //推荐码
    private String referralCode;

}
