package com.lt.fly.web.controller;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IBetGroupRepository;
import com.lt.fly.dao.IGameGroupRepository;
import com.lt.fly.entity.BetGroup;
import com.lt.fly.entity.GameGroup;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.utils.MyBeanUtils;
import com.lt.fly.web.req.BetGroupAdd;
import com.lt.fly.web.vo.BetGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bet")
public class BetGroupController extends BaseController {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IBetGroupRepository iBetGroupRepository;

    @Autowired
    private IGameGroupRepository iGameGroupRepository;

    @PostMapping("/add")
    @UserLoginToken
    public HttpResult add(@RequestBody BetGroupAdd req) throws ClientErrorException {
        GameGroup gameGroup = isNotNull(iGameGroupRepository.findById(req.getGameGroupId()),"传递的参数没有实体");
        BetGroup betGroup = new BetGroup();
        betGroup.setId(idWorker.nextId());
        betGroup.setCreateTime(System.currentTimeMillis());
        betGroup.setModifyTime(System.currentTimeMillis());
        betGroup.setGameGroup(gameGroup);
        MyBeanUtils.copyProperties(req,betGroup);
        iBetGroupRepository.save(betGroup);
        return HttpResult.success(new BetGroupVo(betGroup),"添加"+req.getName()+"下注组成功");
    }
}
