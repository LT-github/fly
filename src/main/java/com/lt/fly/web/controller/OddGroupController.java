package com.lt.fly.web.controller;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IBetGroupRepository;
import com.lt.fly.dao.IGameGroupRepository;
import com.lt.fly.dao.IOddGroupRepository;
import com.lt.fly.dao.IOddRepository;
import com.lt.fly.entity.Odd;
import com.lt.fly.entity.OddGroup;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.req.OddGroupAdd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/odd_group")
public class OddGroupController extends BaseController {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IOddGroupRepository iOddGroupRepository;

    @Autowired
    private IGameGroupRepository iGameGroupRepository;

    @Autowired
    private IBetGroupRepository iBetGroupRepository;

    @Autowired
    private IOddRepository iOddRepository;

    /**
     * 添加赔率组时,先找到所有的玩法组
     * @param req
     * @return
     * @throws ClientErrorException
     */
    @PostMapping("/add")
    @UserLoginToken
    public HttpResult add(OddGroupAdd req)throws ClientErrorException {
        OddGroup oddGroup = new OddGroup();
        oddGroup.setId(idWorker.nextId());
        oddGroup.setCreateTime(System.currentTimeMillis());
        oddGroup.setCreateUser(getLoginUser());
        oddGroup.setName(req.getName());

        List<Odd> odds = iOddRepository.findByIds(req.getOddIds());
        oddGroup.setOdds(new HashSet<>(odds));

        iOddGroupRepository.save(oddGroup);
        return HttpResult.success();
    }

}
