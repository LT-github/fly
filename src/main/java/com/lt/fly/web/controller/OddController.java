package com.lt.fly.web.controller;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IBetGroupRepository;
import com.lt.fly.dao.IOddGroupRepository;
import com.lt.fly.dao.IOddRepository;
import com.lt.fly.entity.BetGroup;
import com.lt.fly.entity.Odd;
import com.lt.fly.entity.OddGroup;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.req.OddAdd;
import com.lt.fly.web.req.OddFind;
import com.lt.fly.web.req.OddGroupAdd;
import com.lt.fly.web.vo.OddGroupVo;
import com.lt.fly.web.vo.OddVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/odd")
public class OddController extends BaseController {

    @Autowired
    private IOddRepository iOddRepository;

    @Autowired
    private IBetGroupRepository iBetGroupRepository;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IOddGroupRepository iOddGroupRepository;

    @PostMapping
    @UserLoginToken
    public HttpResult addOdd(@RequestBody OddAdd req) throws ClientErrorException {
        BetGroup betGroup = isNotNull(iBetGroupRepository.findById(req.getBetGroupId()),"传递的参数没有实体");
        Odd odd = new Odd();
        odd.setId(idWorker.nextId());
        odd.setBetGroup(betGroup);
        odd.setCreateTime(System.currentTimeMillis());
        odd.setCreateUser(getLoginUser());
        odd.setOddValue(req.getOddValue());
        iOddRepository.save(odd);
        return HttpResult.success(new OddVo(odd),"添加下注组'"+betGroup.getName()+"'的赔率成功");
    }

    /**
     * 查找指定下注组和赔率组的赔率
     * @param req
     * @return
     * @throws ClientErrorException
     */
    @GetMapping()
    @UserLoginToken
    public HttpResult findOne(@Validated OddFind req,BindingResult bindingResult) throws ClientErrorException{
        this.paramsValid(bindingResult);
        if(null == req.getOddGroupId()){
            List<Odd> odds = iOddRepository.findByBetGroupId(req.getBetGroupId());
            if (null != odds)
                return HttpResult.success(OddVo.tovo(odds),"获取赔率成功");
        }else{
            Odd odd = iOddRepository.findOne(req.getBetGroupId(),req.getOddGroupId());
            if(null != odd) {
                return HttpResult.success(new OddVo(odd),"获取赔率成功");
            }
        }
        return HttpResult.success(null,"该下注组没有赔率,请添加");

    }



    /**
     * 添加赔率组时,选择对应的赔率
     * @param req
     * @return
     * @throws ClientErrorException
     */
    @PostMapping("/group")
    @UserLoginToken
    public HttpResult addOddGroup(@RequestBody @Validated OddGroupAdd req, BindingResult bindingResult)throws ClientErrorException {
        this.paramsValid(bindingResult);
        OddGroup oddGroup = new OddGroup();
        oddGroup.setId(idWorker.nextId());
        oddGroup.setCreateTime(System.currentTimeMillis());
        oddGroup.setCreateUser(getLoginUser());
        oddGroup.setName(req.getName());

        List<Odd> odds = iOddRepository.findByIds(req.getOddIds());
        oddGroup.setOdds(new HashSet<>(odds));

        iOddGroupRepository.save(oddGroup);
        return HttpResult.success(new OddGroupVo(oddGroup),"添加'"+req.getName()+"'成功");
    }

    @GetMapping("/group")
    @UserLoginToken
    public HttpResult findOddGroups() throws ClientErrorException{
        List<OddGroup> oddGroups = iOddGroupRepository.findAll();
        return HttpResult.success(OddGroupVo.tovo(oddGroups),"查询赔率组成功");
    }

}
