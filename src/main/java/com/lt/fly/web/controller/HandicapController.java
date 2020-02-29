package com.lt.fly.web.controller;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 盘口
 */
@RestController
@RequestMapping("handicap")
public class HandicapController extends BaseController {

    @PostMapping
    @UserLoginToken
    public HttpResult add() throws ClientErrorException {

        return HttpResult.success();
    }
}
