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
import com.lt.fly.web.req.BetGroupFind;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.BetGroupVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.BindingType;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bet")
public class BetGroupController extends BaseController {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IBetGroupRepository iBetGroupRepository;

    @Autowired
    private IGameGroupRepository iGameGroupRepository;

    @PostMapping
    @UserLoginToken
    public HttpResult add(@RequestBody BetGroupAdd req) throws ClientErrorException {
        GameGroup gameGroup = isNotNull(iGameGroupRepository.findById(req.getGameGroupId()),"传递的参数没有实体");
        BetGroup betGroup = new BetGroup();
        betGroup.setId(idWorker.nextId());
        betGroup.setCreateTime(System.currentTimeMillis());
        betGroup.setModifyTime(System.currentTimeMillis());
        betGroup.setCreateUser(getLoginUser());
        betGroup.setGameGroup(gameGroup);
        MyBeanUtils.copyProperties(req,betGroup);
        iBetGroupRepository.save(betGroup);
        return HttpResult.success(new BetGroupVo(betGroup),"添加'"+req.getName()+"'下注组成功");
    }

    @GetMapping
    @UserLoginToken
    public HttpResult find(BetGroupFind req) throws ClientErrorException{
        Page<BetGroup> page = iBetGroupRepository.findAll(req);
        PageResp<BetGroupVo,BetGroup> resp = new PageResp<BetGroupVo,BetGroup>(page).getPageVo(new PageResp.PageGenerator<BetGroupVo,BetGroup>(){

            @Override
            public List<BetGroupVo> generator(List<BetGroup> content) {
                return BetGroupVo.tovo(content);
            }
        });
        return HttpResult.success(resp,"获取下注组列表成功");
    }
}
