package com.lt.fly.web.query;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetReportFind extends DataQueryObjectPage {

   private Long start = DateUtil.getDayStartTime(System.currentTimeMillis());

   private Long end = System.currentTimeMillis();
}
