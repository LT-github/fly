package com.lt.fly.web.vo;

import com.lt.fly.entity.Finance;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.utils.MyBeanUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FinanceVo {
    // 财务的审核状态 0  审核中 1审核通过 2审核失败
    private Integer auditStatus;
    //该订单创建时间
    private Long createTime;
    //审核人昵称
    private String modifyUsername;
    //审核时间
    private Long modifyTime;
    // 这笔财务是属于哪一个用户
    private Long createUserId;
    //这笔财务是属于哪一个用户，用户名
    private String createUsername;
    //该笔充值订单金额
    private Double money;
    //描述
    private String description;
    //用户余额
    private Double balance;
    //用户行为
    private String behavior;
    //财务记录id
    private Long id;

    public FinanceVo() {
        super();
    }

    public FinanceVo(Finance obj) {
        MyBeanUtils.copyProperties(obj,this);
        if (null != obj.getModifyUser())
            this.modifyUsername = obj.getModifyUser().getUsername();
        if(null != obj.getCreateUser())
        this.createUserId = obj.getCreateUser().getId();
        if(null != obj.getCreateUser())
        this.createUsername = obj.getCreateUser().getUsername();
        if (null != GlobalConstant.FinanceType.getFinanceTypeByCode(obj.getType())) {
            this.behavior = GlobalConstant.FinanceType.getFinanceTypeByCode(obj.getType()).getMsg();
        }
    }


    public static List<FinanceVo> tovo(List<Finance> finances){
       List<FinanceVo> list = new ArrayList<>();
        for (Finance item :
                finances) {
            list.add(new FinanceVo(item));
        }
        return list;
    }

}
