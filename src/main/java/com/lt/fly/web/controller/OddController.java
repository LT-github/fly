package com.lt.fly.web.controller;

import com.lt.fly.Service.IOddService;
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
import com.lt.fly.web.query.OddFind;
import com.lt.fly.web.req.OddGroupAdd;
import com.lt.fly.web.req.OddGroupEdit;
import com.lt.fly.web.vo.OddGroupVo;
import com.lt.fly.web.vo.OddVo;
import org.apache.dubbo.remoting.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private IOddService iOddService;


    @PostMapping
    @UserLoginToken
    public HttpResult addOdd(@RequestBody @Validated OddAdd req, BindingResult bindingResult) throws ClientErrorException {
        this.paramsValid(bindingResult);
        BetGroup betGroup = isNotNull(iBetGroupRepository.findById(req.getBetGroupId()),"传递的参数没有实体");
        Set<Odd> odds = betGroup.getOdds();
        for (Odd item :
                odds) {
            if (item.getOddValue().equals(req.getOddValue())){
                throw new ClientErrorException("该赔率组已经存在赔率值为'"+req.getOddValue()+"'的赔率,请勿重复添加");
            }
        }
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
     * 查找指定下注组的赔率
     * @param betGroupId
     * @return
     * @throws ClientErrorException
     */
    @GetMapping("/{betGroupId}")
    @UserLoginToken
    public HttpResult findByBetGroup(@PathVariable Long betGroupId) throws ClientErrorException{
        List<Odd> odds = iOddRepository.findByBetGroupId(betGroupId);
        if(null == odds || 0 == odds.size()){
            return null;
        }
        return HttpResult.success(OddVo.tovo(odds),"查询赔率成功!");
    }

    /**
//     * 查找指定下注组和赔率组的赔率
//     * @param req
//     * @return
//     * @throws ClientErrorException
//     */
//    @GetMapping()
//    @UserLoginToken
//    public HttpResult findOne(@Validated OddFind req,BindingResult bindingResult) throws ClientErrorException{
//        this.paramsValid(bindingResult);
//        if(null != iOddService.(req)){
//            return HttpResult.success(iOddService.findOne(req),"获取赔率成功");
//        }
//        return HttpResult.success(null,"该下注组没有赔率,请添加");
//    }

    /**
     * 删除赔率
     * @return
     * @Param id
     * @throws ClientErrorException
     */
    @DeleteMapping("/{id}")
    @UserLoginToken
    public HttpResult delete(@PathVariable Long id) throws ClientErrorException{
        Odd odd = isNotNull(iOddRepository.findById(id),"传递的参数没有实体");
        if (null != odd.getOddGroups() && !odd.getOddGroups().isEmpty())
            throw new ClientErrorException("该赔率正在被使用,不能删除");
        iOddRepository.delete(odd);
        return HttpResult.success(null,"删除赔率成功");
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
        if (null != req.getOddIds() && 0 != req.getOddIds().size()){
            List<Odd> odds = iOddRepository.findByIds(req.getOddIds());
            oddGroup.setOdds(new HashSet<>(odds));
        }

        iOddGroupRepository.save(oddGroup);
        return HttpResult.success(new OddGroupVo(oddGroup),"添加'"+req.getName()+"'成功");
    }

    /**
     * 获取赔率组列表
     * @return
     * @throws ClientErrorException
     */
    @GetMapping("/group")
    @UserLoginToken
    public HttpResult findOddGroups() throws ClientErrorException{
        List<OddGroup> oddGroups = iOddGroupRepository.findAll();
        return HttpResult.success(OddGroupVo.tovo(oddGroups),"查询赔率组成功");
    }

    /**
     * 删除赔率组
     * @return
     * @throws ClientErrorException
     */
    @DeleteMapping("/group/{id}")
    @UserLoginToken
    public HttpResult deleteOddGroup(@PathVariable Long id) throws ClientErrorException{
        OddGroup oddGroup = isNotNull(iOddGroupRepository.findById(id),"传递的参数没有实体");
        if (null != oddGroup.getHandicap())
            throw new ClientErrorException("该赔率组正在被"+oddGroup.getHandicap().getName()+"盘口使用中,不能删除");
        iOddGroupRepository.delete(oddGroup);
        return HttpResult.success(null,"删除成功");
    }

    public HttpResult editOddGroup(@PathVariable Long id,@RequestBody OddGroupEdit req) throws ClientErrorException{
        OddGroup oddGroup = isNotNull(iOddGroupRepository.findById(id),"传递的参数没有实体");
        oddGroup.setName(req.getName());
        return HttpResult.success(new OddGroupVo(oddGroup),"修改赔率组成功");
    }
}
