package com.lt.fly.web.query;

import com.lt.fly.jpa.support.DataQueryObject;
import com.lt.fly.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetReportFind implements DataQueryObject {

   private Long before = DateUtil.getDayStartTime(System.currentTimeMillis());

   private Long after = System.currentTimeMillis();

   protected Integer page = 1;

   protected Integer size = 10;
}
