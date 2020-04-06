package com.lt.fly.web.controller;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.*;
import com.lt.fly.entity.*;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.log.Log;
import com.lt.fly.web.query.DataDictionaryFind;
import com.lt.fly.web.req.HandicapAdd;
import com.lt.fly.web.req.HandicapQReq;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.HandicapVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * 组接口
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/handicap")
public class HandicapController extends BaseController {

    @Autowired
    private IProportionRepository iProportionRepository;

    @Autowired
    private IDataDictionaryRepository iDataDictionaryRepository;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IHandicapRepository iHandicapRepository;

    @Autowired
    private IMemberRepository iMemberRepository;

    @Autowired
    private IOddGroupRepository iOddGroupRepository;

    /**
     * 添加盘口
     * @param obj
     * @param bindingResult
     * @return
     * @throws Exception
     */
//    @RequiredPermission(value="addUserGroup")
    @PostMapping
    @UserLoginToken
    @Log(value = "添加盘口")
    public HttpResult<HandicapVo> addHandicap(@RequestBody @Validated HandicapAdd obj, BindingResult bindingResult)
            throws Exception{
        this.paramsValid(bindingResult);
        existsForName(iHandicapRepository.findByName(obj.getName()),"盘口名已经存在");

        Handicap objSave = addOrEditorGroup(null,obj);
        return HttpResult.success(new HandicapVo(objSave),"添加成功");
    }

    private Handicap addOrEditorGroup(Long id , HandicapAdd obj) throws Exception, ClientErrorException {

        // 设置基本数据
        Handicap objSave = null;
        if(id != null) {
            objSave = isNotNull( iHandicapRepository.findById(id),"修改的组id不存在");
        }else {
            objSave = new Handicap();
            objSave.setId(idWorker.nextId());
            objSave.setCreateTime(System.currentTimeMillis());
            objSave.setCreateUser(getLoginUser());
        }

        objSave.setName(obj.getName());
        objSave.setModifyTime(System.currentTimeMillis());
        objSave.setModifyUser(getLoginUser());
        // 设置流水、盈亏
        DataDictionary liushui = checkDatadictionary(obj.getLiushuiId(),"liushui-query");
        objSave.setLiushui(liushui);

        DataDictionary yinkui = checkDatadictionary(obj.getYinkunId(),"yinkui-query");
        objSave.setYingkui(yinkui);

        // 设置返点
        List<Long> proportionIds = obj.getProportionIds();
            Set<Proportion> pro=new HashSet<>();
            for(Long proportionId : proportionIds) {
                Proportion proportion = isNotNull(iProportionRepository.findById(proportionId),"添加的返点不存在");
                pro.add(proportion);

        }
        objSave.setProportions(pro);

        //设置赔率组
        OddGroup oddGroup = isNotNull(iOddGroupRepository.findById(obj.getOddGroupId()),"添加的赔率组不存在实体");
        if (null != oddGroup.getHandicap()){
            if (!objSave.getId().equals(oddGroup.getHandicap().getId())) {
                throw new ClientErrorException("当前赔率组正在被"+oddGroup.getHandicap().getName()+"使用中");
            }
        }
        objSave.setOddGroup(oddGroup);

        if (null != objSave.getMembers() && 0 !=objSave.getMembers().size()){
            for (Member item :
                    objSave.getMembers()) {
                item.setHandicap(null);
                item.setIsHaveHandicap(GlobalConstant.IsHaveHandicap.NOT.getCode());
            }
        }

        //设置会员
        if (null != obj.getMemberIds() && 0 != obj.getMemberIds().size()){
            List<Member> members = iMemberRepository.findAllById(obj.getMemberIds());
            for (Member item :
                    members) {
                item.setHandicap(objSave);
                item.setIsHaveHandicap(GlobalConstant.IsHaveHandicap.YSE.getCode());
            }
            objSave.setMembers(new HashSet<>(members));
        }
        Handicap save = iHandicapRepository.save(objSave);
        return save;
    }

    /**
     * 检查数据字典是否存在
     * @param id
     * @param value
     * @return
     * @throws Exception
     */
    private DataDictionary checkDatadictionary(Long id ,String value ) throws Exception {

        Optional<DataDictionary> liushuiOpt = iDataDictionaryRepository.findById(id);
        if(!liushuiOpt.isPresent())
            throw new ClientErrorException("数据字典id传递错误");

        DataDictionary liushuiQuery = iDataDictionaryRepository.findByValue(value);
        if(null!=liushuiQuery) {
            DataDictionaryFind query = new DataDictionaryFind();
            query.setParentId(liushuiQuery.getId());
            List<DataDictionary> list = iDataDictionaryRepository.findAll(query);
            boolean flag = false;
            for(DataDictionary item : list) {
                if(item.getId().equals(id)) {
                    flag = true;
                }
            }
            if(!flag) {
                throw new ClientErrorException("数据字典id传递错误");
            }
            return liushuiOpt.get();
        }else {
            throw new Exception("服务器错误，字典不存在");
        }
    }

    /**
     * 查询所有的盘口,不需要分页
     * @return
     */
//    @RequiredPermission(value="findAllGroup")
    @GetMapping("/all")
    @UserLoginToken
    public HttpResult<List<HandicapVo>> findAll(){
        List<Handicap> handicaps = iHandicapRepository.findAll();
        List<HandicapVo> handicapVos = HandicapVo.toVo(handicaps);
        HandicapVo handicapVo = new HandicapVo();
        handicapVo.setId(GlobalConstant.NoMemberHandicap.ID.getCode());
        handicapVo.setName("未加入盘口");
        handicapVos.add(handicapVo);
        return HttpResult.success(handicapVos,"查询成功");
    }
    /**
     * 根据条件查询盘口
     */
    @PostMapping("/allQ")
    @UserLoginToken
    public HttpResult<List<HandicapVo>> findAllQ(HandicapQReq req){
    	
    	List<Handicap> handicaps = iHandicapRepository.findAllBySettlementType(req.getSettlementType());
    	List<HandicapVo> handicapVos = HandicapVo.toVo(handicaps);
        
        return HttpResult.success(handicapVos,"查询成功");
    }
    
    
    /**
     * 盘口列表,需要分页
     * @param query
     * @return
     */
    @GetMapping
    @UserLoginToken
    @Log(value = "查询盘口列表")
    public HttpResult findAll(DataQueryObjectPage query){
        Page<Handicap> page = iHandicapRepository.findAll(query);
        PageResp resp = new PageResp(page);
        resp.setData(HandicapVo.toVo(page.getContent()));
        return HttpResult.success(resp,"获取盘口列表成功!");
    }

    /**
     * 编辑盘口信息
     * @param id
     * @param obj
     * @param bindingResult
     * @return
     * @throws Exception
     */
//    @RequiredPermission(value="editUserShop-edit")
    @PutMapping("/{id}")
    @UserLoginToken
    @Log(value = "修改盘口信息")
    public HttpResult<HandicapVo> editHandicap(@PathVariable Long id,@RequestBody @Validated HandicapAdd obj,
                                         BindingResult bindingResult) throws Exception{
        this.paramsValid(bindingResult);
        Handicap objEditor = addOrEditorGroup(id, obj);
        return HttpResult.success(new HandicapVo(objEditor),"修改盘口'"+objEditor.getName()+"'成功");
    }

    /**
     * 删除盘口
     * @param id
     * @return
     * @throws ClientErrorException
     */
//    @RequiredPermission(value="delUserShop-del")
    @DeleteMapping("/{id}")
    @UserLoginToken
    @Log(value = "删除盘口")
    public HttpResult deleteHandicap(@PathVariable Long id) throws ClientErrorException {
        Handicap handicap = isNotNull(iHandicapRepository.findById(id),"删除的组id找不到实体");
        if (null != handicap.getMembers() && 0 != handicap.getMembers().size())
            throw new ClientErrorException("该盘口还有会员,请移除后在操作!");
        if (null != handicap.getOddGroup()){
            OddGroup oddGroup = handicap.getOddGroup();
            oddGroup.setHandicap(null);
        }
        iHandicapRepository.delete(handicap);
        return HttpResult.success(null,handicap.getName() + "删除成功");
    }

    /**
     * 主键查找
     * @param id
     * @return
     * @throws ClientErrorException
     */
//    @RequiredPermission(value="findGroupById")
    @GetMapping("/{id}")
    public HttpResult<HandicapVo> findById(@PathVariable Long id) throws ClientErrorException{

        Handicap handicap = isNotNull(iHandicapRepository.findById(id),"删除的组id找不到实体");
        return HttpResult.success(new HandicapVo(handicap), "查询成功");
    }

}