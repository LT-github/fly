package com.lt.fly.web.query;

import com.lt.fly.jpa.support.DataQueryObjectPage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetReportFind extends DataQueryObjectPage {

   private Long start;

   private Long end;
}
