package com.panda.multiterminalinteractivecenter.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.ResponseEnum;
import com.panda.multiterminalinteractivecenter.entity.Role;
import com.panda.multiterminalinteractivecenter.entity.UserRoles;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.service.impl.*;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.multiterminalinteractivecenter.vo.RoleVo;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author :  duwan
 * @Project Name :  test
 * @Package Name :  com.panda.multiterminalinteractivecenter.controller
 * @Description :  TODO
 * @Date: 2022-04-29 13:04:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private MenuServiceImpl menuService;
    private MerchantLogService merchantLogService;

    @Autowired
    private RolePermissionsServiceImpl rolePermissionsService;

    @Autowired
    private DataPermissionsServiceImpl dataPermissionsService;

    @Autowired
    private RoleDataPermissionsServiceImpl roleDataPermissionsService;

    @PostMapping(value = "/queryRoleList")
    public APIResponse<Object> queryRoleList(@RequestBody RoleVo roleVo){
        return roleService.queryRoleList(roleVo);
    }


    @PostMapping(value = "/addRole")
    public APIResponse<Object> addRole(HttpServletRequest request, @RequestBody Role role){
        if (role.getRoleName() == null){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        Role  r= roleService.findRoleByRoleName(role.getRoleName());
        if (r != null){
            return APIResponse.returnFail("角色已存在！");
        }
        r = new Role();
        r.setRoleName(role.getRoleName());
        r.setCreatTime(new Date());
        roleService.addRole(r);
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("addRoles"), null, role.getRoleName());
        merchantLogService.saveLog(MerchantLogPageEnum.USER_MANAGE, MerchantLogTypeEnum.SAVE, filedVO,  null, null, request);
        return APIResponse.returnSuccess();
    }

    @GetMapping(value = "/queryRoleMenuTreeList")
    public APIResponse<Object> queryMenuTreeList(@RequestParam(value = "roleId",required = true)Long roleId){
        return menuService.queryRoleMenuTreeList(roleId);
    }

    @GetMapping(value = "/queryRoleDataList")
    public APIResponse<Object> queryRoleDataList(@RequestParam(value = "roleId",required = true)Long roleId){
        return dataPermissionsService.queryRoleDataList(roleId);
    }

    @PostMapping(value = "/addRoleMenu")
    public APIResponse<Object> addRoleMenu(HttpServletRequest request, @RequestBody RoleVo roleVo){
        if (roleVo.getId() == null){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        rolePermissionsService.addRoleMenu(roleVo, request);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "/addRoleDate")
    public APIResponse<Object> addRoleDate(HttpServletRequest request, @RequestBody RoleVo roleVo){
        if (roleVo.getId() == null){
            return APIResponse.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        roleDataPermissionsService.addRoleDataMenu(roleVo, request);
        return APIResponse.returnSuccess();
    }
}
