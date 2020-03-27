package com.lt.fly.web.query;

import lombok.Data;

@Data
public class ReturnPointFindPage extends MemberFindPage {

    private Integer type;

    private Boolean findStatus = false;

}
