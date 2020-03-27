package com.lt.fly.web.req;

import com.lt.fly.web.vo.ReturnPointVo;
import lombok.Data;

import java.util.List;

@Data
public class ReturnSettleMulity extends ReturnSettle {

    List<Long> ids;

}
