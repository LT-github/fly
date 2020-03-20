package com.lt.fly.web.controller;

import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.ISettingsRepository;
import com.lt.fly.entity.Settings;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.utils.MyBeanUtils;
import com.lt.fly.web.req.SettingAdd;
import com.lt.fly.web.req.SettingEdit;
import com.lt.fly.web.vo.SettingsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("settings")
public class SettingsController extends BaseController {

    @Autowired
    private ISettingsRepository iSettingsRepository;
    @Autowired
    private IdWorker idWorker;

    @PostMapping
    @UserLoginToken
    public HttpResult add(@RequestBody SettingAdd req) throws ClientErrorException {
        Settings settings  = new Settings();
        MyBeanUtils.copyProperties(req,settings);
        settings.setId(idWorker.nextId());
        settings.setCreateTime(System.currentTimeMillis());
        settings.setCreateUser(getLoginUser());
        settings.setModifyTime(System.currentTimeMillis());
        settings.setModifyUser(getLoginUser());
        iSettingsRepository.save(settings);
        return HttpResult.success(new SettingsVo(settings),"添加'"+req.getDataKey()+"'设置选项成功!");
    }

    @PutMapping("/{id}")
    @UserLoginToken
    public HttpResult edit(@RequestBody SettingEdit req, @PathVariable Long id) throws ClientErrorException {
        Settings settings = isNotNull(iSettingsRepository.findById(id),"传递参数没有实体!");
        settings.setDataValue(req.getDataValue());
        settings.setModifyUser(getLoginUser());
        settings.setModifyTime(System.currentTimeMillis());
        iSettingsRepository.flush();
        return HttpResult.success(new SettingsVo(settings),"修改'"+settings.getDataKey()+"'设置选项成功!");
    }


}
