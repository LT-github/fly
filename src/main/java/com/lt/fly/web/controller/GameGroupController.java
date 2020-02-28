package com.lt.fly.web.controller;

import com.lt.fly.dao.IGameGroupRepository;
import com.lt.fly.entity.GameGroup;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.utils.MyBeanUtils;
import com.lt.fly.web.req.GameGroupAdd;
import com.lt.fly.web.vo.GameGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameGroupController extends BaseController {

    @Autowired
    private IGameGroupRepository iGameGroupRepository;

    @Autowired
    private IdWorker idWorker;

    //只添加玩法组
    @PostMapping("/add")
    public HttpResult add(@RequestBody @Validated GameGroupAdd req, BindingResult bindingResult) throws ClientErrorException {
        this.paramsValid(bindingResult);
        GameGroup gameGroup = new GameGroup();
        MyBeanUtils.copyProperties(req,gameGroup);
        gameGroup.setId(idWorker.nextId());
        gameGroup.setCreateTime(System.currentTimeMillis());
        gameGroup.setModifyTime(System.currentTimeMillis());
        gameGroup.setAddType(GlobalConstant.GameGroupAddType.DEFUALt.getCode());
        iGameGroupRepository.save(gameGroup);
        return HttpResult.success(new GameGroupVo(gameGroup),"添加"+req.getName()+"玩法组成功");
    }

}
