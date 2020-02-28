package com.lt.fly.web.controller;

import com.lt.fly.dao.IBetGroupRepository;
import com.lt.fly.dao.IOddRepository;
import com.lt.fly.entity.BetGroup;
import com.lt.fly.entity.Odd;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.web.req.OddAdd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/odd")
public class OddController extends BaseController {

    @Autowired
    private IOddRepository iOddRepository;

    @Autowired
    private IBetGroupRepository iBetGroupRepository;

    public HttpResult add(OddAdd req) throws ClientErrorException {
        BetGroup betGroup = isNotNull(iBetGroupRepository.findById(req.getBetGroupId()),"传递的参数没有实体");
        Odd odd = new Odd();
        odd.setBetGroup(betGroup);
        odd.setCreateTime(System.currentTimeMillis());
        odd.setCreateUser(getLoginUser());
        odd.setOddValue(req.getOddValue());
        return HttpResult.success();
    }

}
