package com.lt.fly.web.resp;

import lombok.Data;

@Data
public class DetailsResp<T, L> extends PageResp<T, L> {

    private Long issuNumber;
}
