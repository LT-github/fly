package com.lt.fly.web.controller;

import com.lt.fly.Service.IOddService;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IBetGroupRepository;
import com.lt.fly.dao.IGameGroupRepository;
import com.lt.fly.dao.IOddGroupRepository;
import com.lt.fly.dao.IOddRepository;
import com.lt.fly.entity.BetGroup;
import com.lt.fly.entity.GameGroup;
import com.lt.fly.entity.Odd;
import com.lt.fly.entity.OddGroup;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.utils.MyBeanUtils;
import com.lt.fly.web.query.BetGroupFind;
import com.lt.fly.web.query.GameGroupFind;
import com.lt.fly.web.query.OddFind;
import com.lt.fly.web.req.*;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.BetGroupVo;
import com.lt.fly.web.vo.GameForAddVo;
import com.lt.fly.web.vo.GameGroupVo;
import com.lt.fly.web.vo.GameTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 玩法
 */
@RestController
@RequestMapping("/game")
public class GameController extends BaseController {

    @Autowired
    private IGameGroupRepository iGameGroupRepository;

    @Autowired
    private IBetGroupRepository iBetGroupRepository;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IOddRepository iOddRepository;

    @Autowired
    private IOddGroupRepository iOddGroupRepository;

    @Autowired
    private IOddService iOddService;

    /**
     * 添加玩法组
     * @param req
     * @param bindingResult
     * @return
     * @throws ClientErrorException
     */
    @PostMapping
    @UserLoginToken
    public HttpResult add(@RequestBody @Validated GameGroupAdd req, BindingResult bindingResult) throws ClientErrorException {
        this.paramsValid(bindingResult);
        GameGroup gameGroup = new GameGroup();
        MyBeanUtils.copyProperties(req,gameGroup);
        gameGroup.setId(idWorker.nextId());
        gameGroup.setCreateTime(System.currentTimeMillis());
        gameGroup.setModifyTime(System.currentTimeMillis());
        iGameGroupRepository.save(gameGroup);
        return HttpResult.success(new GameGroupVo(gameGroup),"添加'"+req.getName()+"'玩法组成功");
    }

    /**
     * 查询玩法组
     * @param req
     * @return
     * @throws ClientErrorException
     */
    @GetMapping
    @UserLoginToken
    public HttpResult gameFind(GameGroupFind req) throws ClientErrorException{
        List<GameGroup> gameGroups = iGameGroupRepository.findAll(req);
        return HttpResult.success(GameGroupVo.tovo(gameGroups),"查询玩法组成功");
    }

    /**
     * 修改玩法组信息
     * @param id
     * @param req
     * @return
     * @throws ClientErrorException
     */
    @PutMapping("/{id}")
    @UserLoginToken
    public HttpResult gameEdit(@PathVariable Long id,@RequestBody GameGroupEdit req) throws ClientErrorException{
        GameGroup gameGroup = isNotNull(iGameGroupRepository.findById(id),"传递的参数没有实体");
        MyBeanUtils.copyProperties(req,gameGroup);
        return HttpResult.success(new GameGroupVo(gameGroup),"修改"+ gameGroup.getName()+"信息成功");
    }

    /**
     * 彩种列表
     * @return
     * @throws ClientErrorException
     */
    @GetMapping("/type")
    @UserLoginToken
    public HttpResult findGameType() throws ClientErrorException{
        Map<String,Integer> map = new HashMap<>();
        List<GameGroup> gameGroups= iGameGroupRepository.findAll();
        for (GameGroup gameGroup : gameGroups) {
            //返回值是否为空
            if(map.isEmpty()) {
                map.put(gameGroup.getType(), gameGroup.getStatus());
                continue;
            }
            if(map.containsKey(gameGroup.getType()) && map.get(gameGroup.getType()) == GlobalConstant.GameStatus.OPEN.getCode()) {
                map.put(gameGroup.getType(), GlobalConstant.GameStatus.OPEN.getCode());
            }else {
                map.put(gameGroup.getType(), gameGroup.getStatus());
            }
        }
        Set<GameTypeVo> gameTypeVos = new HashSet<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            GameTypeVo gameTypeVo = new GameTypeVo();
            gameTypeVo.setType(entry.getKey());
            gameTypeVo.setStatus(entry.getValue());
            gameTypeVos.add(gameTypeVo);
        }
        return HttpResult.success(gameTypeVos,"彩种列表查询成功");
    }

    @PutMapping("/type")
    @UserLoginToken
    public HttpResult editType(GameTypeEdit req) throws ClientErrorException{
        List<GameGroup> gameGroups = iGameGroupRepository.findByType(req.getType());
        if(null == gameGroups || 0 == gameGroups.size())
            throw new ClientErrorException("当前的参数"+req.getType()+"没有实体");

        for (GameGroup item : gameGroups) {
            MyBeanUtils.copyProperties(req, item);
            for (BetGroup betGroup : item.getBetGroups()) {
                //最后修改时间和修改人
                betGroup.setModifyTime(System.currentTimeMillis());
                betGroup.setModifyUser(getLoginUser());
                betGroup.setStatus(req.getStatus());
            }
        }
        iGameGroupRepository.saveAll(gameGroups);
        return HttpResult.success(null,"修改彩种状态成功");
    }

    /**
     * 添加下注组
     * @param req
     * @return
     * @throws ClientErrorException
     */
    @PostMapping("/bet")
    @UserLoginToken
    public HttpResult add(@RequestBody BetGroupAdd req) throws ClientErrorException {
        GameGroup gameGroup = isNotNull(iGameGroupRepository.findById(req.getGameGroupId()),"传递的参数没有实体");
        BetGroup betGroup = new BetGroup();
        betGroup.setId(idWorker.nextId());
        betGroup.setCreateTime(System.currentTimeMillis());
        betGroup.setModifyTime(System.currentTimeMillis());
        betGroup.setCreateUser(getLoginUser());
        betGroup.setModifyUser(getLoginUser());
        betGroup.setGameGroup(gameGroup);
        MyBeanUtils.copyProperties(req,betGroup);
        iBetGroupRepository.save(betGroup);
        return HttpResult.success(new BetGroupVo(betGroup,null),"添加'"+req.getName()+"'成功");
    }

    /**
     * 查询下注组
     * @param query
     * @return
     * @throws ClientErrorException
     */
    @GetMapping("/bet")
    @UserLoginToken
    public HttpResult find(BetGroupFind query) throws ClientErrorException{
        Page<BetGroup> page = iBetGroupRepository.findAll(query);
        OddGroup oddGroup = null;
        if(null != query.getOddGroupId()){
            oddGroup = isNotNull(iOddGroupRepository.findById(query.getOddGroupId()), "传递的参数没有实体");
        }

        PageResp resp = new PageResp(page);
        List<BetGroupVo> betGroupVos = new ArrayList<>();

        if (null != oddGroup) {

            for (BetGroup item :
                    page) {
                BetGroupVo betGroupVo = new BetGroupVo(item,null);
                for (Odd odd :
                        oddGroup.getOdds()) {
                    if (odd.getBetGroup().getId().equals(item.getId())) {
                        betGroupVo.setOddValue(odd.getOddValue());
                    }
                }
                betGroupVos.add(betGroupVo);

            }
        }else {
            for (BetGroup item :
                    page) {
                betGroupVos.add(new BetGroupVo(item,null));
            }
        }
        resp.setData(betGroupVos);
        return HttpResult.success(resp,"获取下注组列表成功");
    }

    /**
     * 修改下注组,如果oddId不为null则修改赔率
     * @param id
     * @param req
     * @return
     * @throws ClientErrorException
     */
    @PutMapping("/bet/{id}")
    @UserLoginToken
    public HttpResult edit(@PathVariable Long id , @RequestBody BetGroupEdit req) throws ClientErrorException{
        BetGroup betGroup = isNotNull(iBetGroupRepository.findById(id),"传递的参数没有实体");
        MyBeanUtils.copyProperties(req,betGroup);
        iBetGroupRepository.save(betGroup);

        Odd odd = null;
        if(null != req.getOddId()){
            odd = isNotNull(iOddRepository.findById(req.getOddId()),"传递的参数没有实体");
            OddGroup oddGroup = isNotNull(iOddGroupRepository.findById(req.getOddGroupId()),"传递的参数没有实体");
            Set<Odd> odds = new HashSet<>();
            //找到该下注组的所有赔率并且循环
            if (null != oddGroup.getOdds() || 0 !=oddGroup.getOdds().size()) {
                for (Odd item :
                        oddGroup.getOdds()) {
                    if(!item.getBetGroup().getId().equals(id)){
                        odds.add(item);
                    }
                }
                odds.add(odd);
            }else{
                odds.add(odd);
            }


            oddGroup.setOdds(odds);
            iOddGroupRepository.save(oddGroup);
        }
        return HttpResult.success(new BetGroupVo(betGroup,odd),"修改"+betGroup.getName()+"信息成功");
    }

    /**
     * 多选修改下注组
     * @param req
     * @return
     * @throws ClientErrorException
     */
    @PostMapping("/bet/mul")
    @UserLoginToken
    public HttpResult editMulti(@RequestBody BetGroupEditMulti req) throws ClientErrorException{
        List<BetGroup> betGroups = iBetGroupRepository.findAllById(req.getIds());
        if(null == betGroups || 0 == betGroups.size())
            throw new ClientErrorException("当前没有玩法在使用!请联系管理员!");

        for (BetGroup item : betGroups) {
            MyBeanUtils.copyProperties(req, item);
            //最后修改时间和修改人
            item.setModifyTime(System.currentTimeMillis());
            item.setModifyUser(getLoginUser());
        }
        iBetGroupRepository.saveAll(betGroups);
        return HttpResult.success(BetGroupVo.tovo(betGroups),"多选修改下注组成功!");
    }

    /**
     * 添加赔率时获取玩法列表
     * @return
     * @throws ClientErrorException
     */
    @GetMapping("all")
    @UserLoginToken
    public HttpResult getALlforAdd() throws ClientErrorException{
        List<BetGroup> betGroups = iBetGroupRepository.findAll();
        return HttpResult.success(GameForAddVo.tovo(betGroups),"获取玩法列表成功!");
    }
}
