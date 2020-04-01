package com.lt.fly.web.vo;

import com.lt.fly.entity.Member;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReferrerMemberVo {

    private Long id;

    //推荐人用户名
    private String referralName;

    //推手用户名
    private String username;

    //推手昵称
    private String nickname;

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

    //状态
    private Integer status;

    //返点比例
    private List<ProportionVo> proportionVos;


    public ReferrerMemberVo(Member obj) {
        BeanUtils.copyProperties(obj,this);
        if (null != obj.getModifyUser()){
            this.referralName = obj.getModifyUser().getUsername();
        }
        if (null != obj.getProportions() && 0 !=obj.getProportions().size()){
            this.proportionVos = ProportionVo.toVo(new ArrayList<>(obj.getProportions()));
        }
    }
}
