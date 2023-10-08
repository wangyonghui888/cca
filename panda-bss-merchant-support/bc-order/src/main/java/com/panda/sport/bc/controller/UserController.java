package com.panda.sport.bc.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.bc.service.impl.BcAdminRoleServiceImpl;
import com.panda.sport.bc.service.impl.BcAdminUserServiceImpl;
import com.panda.sport.bc.utils.SecurityUtils;
import com.panda.sport.bc.vo.BcAdminUserVo;
import com.panda.sport.bc.vo.form.BcAdminUserForm;
import com.panda.sport.bc.vo.form.BcEditPwdForm;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bcorder.BcAdminRole;
import com.panda.sport.merchant.common.po.bcorder.BcAdminUser;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName UserController
 * @auth YK
 * @Description 后台用户管理
 * @Date 2020-09-03 13:51
 * @Version
 */
@Slf4j
@RestController
@RequestMapping("/bc/user")
public class UserController {

    @Autowired
    private BcAdminUserServiceImpl adminUserService;

    @Autowired
    private BcAdminRoleServiceImpl adminRoleService;

    /**
     * @description: 用户列表
     * @Param: [username, pageNum, enabled, pageSize]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @author: YK
     * @date: 2020/9/3 14:12
     */
    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('user')")
    public Response list(@RequestParam(name = "username", defaultValue = "") String username,
                         @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "enabled", defaultValue = "2") Integer enabled,
                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {


        PageHelper.startPage(pageNum, pageSize, true);
        QueryWrapper<BcAdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        if (enabled != 2) {
            // 传2 是所有
            queryWrapper.eq("enabled", enabled);
        }
        queryWrapper.orderByDesc("create_time");
        List<BcAdminUser> AdminUserList = adminUserService.list(queryWrapper);
        List<Long> roleIdS = AdminUserList.stream().map(a -> a.getRoleId()).collect(Collectors.toList());
        Map<Long, String> roleMap = Collections.EMPTY_MAP;
        Map<Long, String> roleEnMap = Collections.EMPTY_MAP;
        if (roleIdS.size() > 0) {
            List<BcAdminRole> adminRoleList = adminRoleService.list(new QueryWrapper<BcAdminRole>().in("id", roleIdS));
            roleMap = adminRoleList.stream().collect(Collectors.toMap(BcAdminRole::getId, BcAdminRole::getName));
            roleEnMap = adminRoleList.stream().collect(Collectors.toMap(BcAdminRole::getId, BcAdminRole::getEn));
        }
        List<BcAdminUserVo> adminUserVoList = new ArrayList<>();
        for (BcAdminUser a : AdminUserList) {
            BcAdminUserVo adminUserVo = new BcAdminUserVo();
            BeanUtils.copyProperties(a, adminUserVo);
            adminUserVo.setRoleName(roleMap.containsKey(a.getRoleId()) ? roleMap.get(a.getRoleId()) : "");
            adminUserVo.setRoleNameEn(roleEnMap.containsKey(a.getRoleId()) ? roleEnMap.get(a.getRoleId()) : "");
            adminUserVoList.add(adminUserVo);
        }
        PageInfo<BcAdminUserVo> pageInfo = new PageInfo<>(adminUserVoList);
        return Response.returnSuccess(pageInfo);
    }


    /**
     * @description: 添加后台用户
     * @Param: [adminUserForm, bindingResult]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @author: YK
     * @date: 2020/9/3 14:12
     */
    @PostMapping("/add")
    public Response add(@Validated @RequestBody BcAdminUserForm adminUserForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID, bindingResult.getFieldError().getDefaultMessage());
        }
        if (StringUtils.isEmpty(adminUserForm.getPassword())) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID, "密码必传");
        }
        BcAdminUser adminUserResult = adminUserService.getOne(new QueryWrapper<BcAdminUser>().eq("username", adminUserForm.getUsername()));
        if (!StringUtils.isEmpty(adminUserResult)) {
            return Response.returnFail("该用户名已经存在");
        }
        BcAdminUser adminUser = new BcAdminUser();
        BeanUtils.copyProperties(adminUserForm, adminUser);

        adminUser.setPassword(SecurityUtils.encryptPassword(adminUserForm.getPassword()));
        adminUser.setCreateTime(DateUtil.now());
        adminUser.setLastPasswordResetTime(DateUtil.now());
        try {
            adminUserService.save(adminUser);
            return Response.returnSuccess("添加成功");
        } catch (Exception e) {
            log.error("添加后台管理用户失败:{}", e.getMessage());
            return Response.returnFail("添加失败");
        }
    }

    /**
     * @description: 编辑用户
     * @Param: [adminUserForm, bindingResult]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @author: YK
     * @date: 2020/9/3 14:23
     */
    @PostMapping("/editPost")
    public Response editPost(@Validated @RequestBody BcAdminUserForm adminUserForm, BindingResult bindingResult) {

        if (StringUtils.isEmpty(adminUserForm.getId())) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        BcAdminUser adminUserResult = adminUserService.getById(adminUserForm.getId());
        if (StringUtils.isEmpty(adminUserResult)) {
            return Response.returnFail("用户不存在");
        }
        BcAdminUser adminUserResultTwo = adminUserService.getOne(new QueryWrapper<BcAdminUser>().eq("username", adminUserForm.getUsername()));
        if (!StringUtils.isEmpty(adminUserResultTwo)) {
            if (!adminUserResultTwo.getId().equals(adminUserForm.getId())) {
                return Response.returnFail("该用户名已经存在");
            }
        }

        BcAdminUser adminUser = new BcAdminUser();
        BeanUtils.copyProperties(adminUserForm, adminUser);
        if (!StringUtils.isEmpty(adminUserForm.getPassword())) {
            // 如果编辑修改了密码
            adminUser.setPassword(SecurityUtils.encryptPassword(adminUserForm.getPassword()));
            adminUser.setLastPasswordResetTime(DateUtil.now());
        } else {
            adminUser.setPassword(adminUserResultTwo.getPassword());
        }
        try {
            adminUserService.updateById(adminUser);
            return Response.returnSuccess("更新成功");
        } catch (Exception e) {
            log.error("更新后台管理用户失败:{}", e.getMessage());
            return Response.returnFail("更新失败");
        }
    }


    /**
     * @description: 删除
     * @Param: [id]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @author: YK
     * @date: 2020/9/3 14:23
     */
    @PostMapping("/delete")
    public Response delete(Integer id) {

        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        BcAdminUser adminUserResult = adminUserService.getById(id);
        if (StringUtils.isEmpty(adminUserResult)) {
            return Response.returnFail("用户不存在");
        }
        boolean isDelete = adminUserService.removeById(id);
        if (isDelete) {
            return Response.returnSuccess();
        }
        return Response.returnFail("删除失败");
    }


    /**
     * @description: 修改密码
     * @Param: [editPwdForm, bindingResult]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @author: YK
     * @date: 2020/9/3 14:23
     */
    @PostMapping("/editPwd")
    public Response editPwd(@Validated @RequestBody BcEditPwdForm editPwdForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        if (editPwdForm.getPassword().length() < 6 || editPwdForm.getPassword().length() > 20) {
            return Response.returnFail("密码长度为6到20位");
        }
        BcAdminUser adminUserResult = adminUserService.getOne(new QueryWrapper<BcAdminUser>().eq("username", editPwdForm.getUsername()));
        if (StringUtils.isEmpty(adminUserResult)) {
            return Response.returnFail("该用户不存在");
        }
        if (!adminUserResult.getPassword().equals(SecurityUtils.encryptPassword(editPwdForm.getOldPassword()))) {
            return Response.returnFail("原密码错误");
        }
        if (!editPwdForm.getPassword().equals(editPwdForm.getSurePassword())) {
            return Response.returnFail("两次密码不一致");
        }
        if (editPwdForm.getOldPassword().equals(editPwdForm.getPassword())) {
            return Response.returnFail("新旧密码一致,请重新输入新密码");
        }

        BcAdminUser adminUser = new BcAdminUser();
        adminUser.setId(adminUserResult.getId());
        adminUser.setPassword(SecurityUtils.encryptPassword(editPwdForm.getPassword()));
        adminUser.setLastPasswordResetTime(DateUtil.now());

        try {
            adminUserService.updateById(adminUser);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("更新用户密码失败:{}", e.getMessage());
            return Response.returnFail("更新失败");
        }
    }

}
