package com.lt.fly.web.controller;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.ISysLogRepository;
import com.lt.fly.entity.SysLog;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.LogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("log")
public class SysLogController {

    @Autowired
    private ISysLogRepository iSysLogRepository;


    @GetMapping
    @UserLoginToken
    public HttpResult find(DataQueryObjectPage query) throws ClientErrorException {
        Page<SysLog> page  = iSysLogRepository.findAll(query);
        PageResp resp = new PageResp(page);
        resp.setData(LogVo.tovo(page.getContent()));
        return HttpResult.success(resp,"获取系统操作日志列表成功");
    }


}
