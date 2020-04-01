package com.lt.fly.web.query;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetReportFind extends DataQueryObjectPage {

   private Long before = DateUtil.getDayStartTime(System.currentTimeMillis());

   private Long after = System.currentTimeMillis();
}
