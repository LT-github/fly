package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TicketAdd {
    private List<BetsOrderAdd> betsOrderAddReqs = new ArrayList<>();//所有注单
}
