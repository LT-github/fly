package com.lt.fly.web.controller;

import com.lt.fly.Service.IUserService;
import com.lt.fly.annotation.RequiredPermission;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IAdminRepository;
import com.lt.fly.dao.IRoleRepository;
import com.lt.fly.entity.Admin;
import com.lt.fly.entity.Role;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.jpa.support.DataQueryObjectPage;
import com.lt.fly.utils.GlobalConstant;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.req.AdminAdd;
import com.lt.fly.web.req.AdminEdit;
import com.lt.fly.web.req.UserLogin;
import com.lt.fly.web.resp.PageResp;
import com.lt.fly.web.resp.UserLoginResp;
import com.lt.fly.web.vo.AdminVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IAdminRepository iAdminRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    //系统用户登录
    @PostMapping("/login")
    public HttpResult<UserLoginResp> login(@RequestBody UserLogin req) {
        return HttpResult.success(iUserService.login(req),"登录成功");
    }

    /**
     * 添加一个系统用户、并赋予权限
     * @param obj
     * @param bindingResult
     * @return
     * @throws ClientErrorException
     */
    @RequiredPermission(value="addUser-add")
    @PostMapping
    @UserLoginToken
    public HttpResult<AdminVo> addAdmin(@RequestBody @Validated AdminAdd obj ,
                                        BindingResult bindingResult) throws ClientErrorException{
        this.paramsValid(bindingResult);
        Admin admin = editOrAddAdmin(obj, null);
        iAdminRepository.save(admin);
        return HttpResult.success(new AdminVo(admin), "添加成功");
    }

    /**
     * 修改系统用户
     * @param obj
     * @param bindingResult
     * @return
     * @throws ClientErrorException
     */
    @RequiredPermission(value="editUser-edit")
    @PutMapping("/{id}")
    @UserLoginToken
    public HttpResult<AdminVo> editAdmin(@PathVariable Long id,@RequestBody @Validated AdminEdit obj ,
                                         BindingResult bindingResult) throws ClientErrorException{

        Admin admin = editOrAddAdmin(obj, id);
        iAdminRepository.save(admin);
        return HttpResult.success(new AdminVo(admin), admin.getNickname() + "修改成功");
    }

    /**
     * 编辑系统用户状态
     * @param id
     * @return
     * @throws ClientErrorException
     */
    @RequiredPermission(value="deleteAdmin")
    @DeleteMapping("/{id}")
    @UserLoginToken
    public HttpResult<Object> deleteAdmin(@PathVariable Long id) throws ClientErrorException{
        Admin admin = isNotNull(iAdminRepository.findById(id),"修改的实体不存在");
        admin.setStatus(GlobalConstant.UserStatus.PROHIBIT.getCode());
        iAdminRepository.save(admin);

        return HttpResult.success(null, admin.getNickname() + "被封停");
    }

    /**
     * 查询所有系统用户
     * @param query
     * @return
     */
    @RequiredPermission(value="getUser")
    @GetMapping("/all")
    @UserLoginToken
    public HttpResult<PageResp<AdminVo, Admin>> findAllByPage(DataQueryObjectPage query){

        Page<Admin> page = iAdminRepository.findAll(query);

        PageResp<AdminVo,Admin> resp = new PageResp<AdminVo, Admin>(page);
        resp.setData(AdminVo.toVo(page.getContent()));
        return HttpResult.success(resp, "查询成功");
    }


    private Admin editOrAddAdmin(AdminEdit obj , Long id) throws ClientErrorException {

        Admin admin = null;

        // 封装基本信息
        if(id != null) {
            admin = isNotNull(iAdminRepository.findById(id),"修改的实体不存在");
            BeanUtils.copyProperties(obj, admin);
            admin.setCreateTime(System.currentTimeMillis());
            admin.setCreateUser(getLoginUser());
        }else {
            AdminAdd objAdd = (AdminAdd) obj;
            admin = new Admin();
            admin.setUsername(objAdd.getUsername());
            admin.setId(idWorker.nextId());
            admin.setCreateTime(System.currentTimeMillis());
            admin.setCreateUser(this.getLoginUser());
            BeanUtils.copyProperties(objAdd, admin);
        }
        admin.setModifyTime(System.currentTimeMillis());
        admin.setModifyUser(getLoginUser());


        //清空角色列表
        admin.getRoles().clear();
        // 封装角色
        List<Long> roleIds = obj.getRoleIds();
        for (Long roleId : roleIds) {
            Role role = isNotNull(iRoleRepository.findById(roleId),"关联的角色id查询不到实体");
            admin.getRoles().add(role);
        }

        return admin;
    }

}
