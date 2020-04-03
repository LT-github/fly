package com.lt.fly.web.controller;

import com.lt.fly.Service.IOrderService;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.web.log.Log;
import com.lt.fly.web.query.BetReportFind;
import com.lt.fly.web.query.DetailsFind;
import com.lt.fly.web.query.OrderFind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Autowired
    private IOrderService iOrderService;

    /**
     * 竞猜列表
     * @param req
     * @return
     * @throws ClientErrorException
     */
//    @RequiredPermission(value="findGuessingList")
    @GetMapping("/all")
    @UserLoginToken
    @Log(value = "查询竞猜列表")
    public HttpResult findAll(OrderFind req) throws ClientErrorException {
        return HttpResult.success(iOrderService.findAll(req),"查询竞猜记录列表成功");
    }

    /**
     * 竞猜报表
     * @return
     * @throws ClientErrorException
     */
    @GetMapping("/report")
    @UserLoginToken
    @Log(value = "查询竞猜报表")
    public HttpResult report(BetReportFind query) throws ClientErrorException{
        return HttpResult.success(iOrderService.findReport(query),"查询竞猜报表成功");
    }

    /**
     * 报表详情
     * @return
     * @throws ClientErrorException
     */
    @GetMapping("/details")
    @UserLoginToken
    @Log(value = "查询报表详情页")
    public HttpResult details(DetailsFind query) throws ClientErrorException{
        return HttpResult.success(iOrderService.details(query),"获取详情页列表成功");
    }

}
