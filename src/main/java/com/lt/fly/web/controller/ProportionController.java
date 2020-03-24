package com.lt.fly.web.controller;

import com.lt.fly.annotation.RequiredPermission;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IDataDictionaryRepository;
import com.lt.fly.dao.IProportionRepository;
import com.lt.fly.entity.DataDictionary;
import com.lt.fly.entity.Proportion;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.utils.CommonsUtil;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.req.ProportionAdd;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.vo.ProportionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 返点
 */
@RestController
@RequestMapping("/proportion")
public class ProportionController extends BaseController {

    @Autowired
    private IProportionRepository iProportionRepository;

    @Autowired
    private IDataDictionaryRepository iDataDictionaryRepository;

    @Autowired
    private IdWorker idWorker;

    /**
     * 添加返点比例
     * @param obj
     * @param bindingResult
     * @return
     * @throws ClientErrorException
     */
    @PostMapping()
    @UserLoginToken
    public HttpResult<ProportionVo> addProportion(@RequestBody @Validated ProportionAdd obj, BindingResult bindingResult)
            throws ClientErrorException {
        this.paramsValid(bindingResult);

        existsForName(iProportionRepository.findByDescription(obj.getDescription()),"返点比例名已经存在");

        Proportion objSave = new Proportion();
        DataDictionary returnPoint = isNotNull(iDataDictionaryRepository.findById(obj.getReturnPointTypeId()),"返点比例不存在");

        if(CommonsUtil.RANGE_LIUSHUI_RETURN_POINT .equals(returnPoint.getId()) || CommonsUtil.RANGE_YINGLI_RETURN_POINT .equals(returnPoint.getId())
        ||CommonsUtil.REFERRAL_LIUSHUI_RETURN_POINT.equals(returnPoint.getId()) || CommonsUtil.REFERRAL_YINGLI_RETURN_POINT.equals(returnPoint.getId())) {
            if(null != obj.getRangeMoney()) {
                objSave.setRanges(obj.getRangeMoney());
            }else {
                throw new ClientErrorException("范围不能为空");
            }
        }

        objSave.setId(idWorker.nextId());
        objSave.setCreateTime(System.currentTimeMillis());
        objSave.setDescription(obj.getDescription());
        objSave.setProportionVal(obj.getProportionVal());
        objSave.setReturnPoint(returnPoint);
        iProportionRepository.save(objSave);
        return HttpResult.success(new ProportionVo(objSave), "添加成功");
    }

    /**
     * 查询返点比例列表
     * @param req
     * @return
     */
//    @RequiredPermission(value="findProportionAll")
    @GetMapping
    @UserLoginToken
    public HttpResult<PageResp<ProportionVo, Proportion>> findProportionAll(DataQueryObjectPage req){
        Page<Proportion> page = iProportionRepository.findAll(req);
        PageResp<ProportionVo, Proportion> resp=  new PageResp<ProportionVo,Proportion>(page).getPageVo(new PageResp.PageGenerator<ProportionVo,Proportion>(){

            @Override
            public List<ProportionVo> generator(List<Proportion> content) {
                return ProportionVo.toVo(content);
            }
        });

        return HttpResult.success(resp,"查询成功");
    }

    /**
     * 获取返点比例列表.不分页
     * @return
     */
    @GetMapping("/all")
    @UserLoginToken
    public HttpResult proportionList(){
        List<Proportion> proportions = iProportionRepository.findAll();

        return HttpResult.success(ProportionVo.toVo(proportions),"获取返点规则列表成功");
    }

    /**
     * 根据id查询返点比例
     * @param id
     * @return
     * @throws ClientErrorException
     */
//    @RequiredPermission(value="findProportionById")
    @GetMapping("/{id}")
    public HttpResult<ProportionVo> findProportionById(@PathVariable Long id) throws ClientErrorException{
        Proportion objQuery = isNotNull(iProportionRepository.findById(id),"查询的返点比例不存在");

        return HttpResult.success(new ProportionVo(objQuery),"查询成功");
    }

    /**
     * 编辑返点比例
     * @param id
     * @param obj
     * @param bindingResult
     * @return
     * @throws ClientErrorException
     */
//    @RequiredPermission(value="editProportion")
    @PutMapping("/{id}")
    @UserLoginToken
    public HttpResult<ProportionVo> editProportion(@PathVariable Long id ,@RequestBody @Validated ProportionAdd obj ,
                                                   BindingResult bindingResult)throws ClientErrorException{
        this.paramsValid(bindingResult);

        Proportion editObj = isNotNull(iProportionRepository.findById(id),"编辑的返点比例不存在");
        if (null != editObj.getHandicaps() || 0 < editObj.getHandicaps().size())
            throw new ClientErrorException("该返点比例还有盘口在使用,请移除后在操作!");

        editObj.setDescription(obj.getDescription());
        editObj.setProportionVal(obj.getProportionVal());

        DataDictionary returnPoint = isNotNull(iDataDictionaryRepository.findById(obj.getReturnPointTypeId()),"返点比例不存在");
        editObj.setReturnPoint(returnPoint);
        if(CommonsUtil.RANGE_LIUSHUI_RETURN_POINT .equals(returnPoint.getId()) || CommonsUtil.RANGE_YINGLI_RETURN_POINT .equals(returnPoint.getId())) {
            if(null != obj.getRangeMoney()) {
                editObj.setRanges(obj.getRangeMoney());
            }else {
                throw new ClientErrorException("范围不能为空");
            }
        }else {
            editObj.setRanges(null);
        }


        iProportionRepository.save(editObj);
        return HttpResult.success(new ProportionVo(editObj) , "编辑成功");
    }

    /**
     * 删除返点比例
     * @param id
     * @return
     * @throws ClientErrorException
     */
//    @RequiredPermission(value="deleteProportion")
    @DeleteMapping("/{id}")
    @UserLoginToken
    public HttpResult<Object> deleteProportion(@PathVariable Long id)
            throws ClientErrorException{

        Proportion objDelete = isNotNull(iProportionRepository.findById(id),"编辑的返点比例不存在");
        if (null != objDelete.getHandicaps() || 0 < objDelete.getHandicaps().size())
            throw new ClientErrorException("该返点比例还有盘口在使用,请移除后在操作!");

        objDelete.getHandicaps().clear();
        iProportionRepository.delete(objDelete);

        return HttpResult.success(null, objDelete.getDescription() + "删除成功");
    }
}
