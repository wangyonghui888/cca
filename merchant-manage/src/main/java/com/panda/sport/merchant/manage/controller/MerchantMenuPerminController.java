package com.panda.sport.merchant.manage.controller;

import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.permission.AuthSeaMoonKey;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.entity.form.MpForm;
import com.panda.sport.merchant.manage.entity.form.PerForm;
import com.panda.sport.merchant.manage.entity.form.RoleMenuForm;
import com.panda.sport.merchant.manage.service.MerchantLevelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 商户权限
 */
@RestController
@RequestMapping("/manage/mp")
@Slf4j
public class MerchantMenuPerminController {


    @Autowired
    private MerchantLevelService merchantLevelService;


    /**
     * 获取所属商户的所有权限和菜单
     * @return
     */
    @PostMapping("/getAll")
    public Response getAll(@RequestBody MpForm mpForm) {

        if (StringUtils.isEmpty(mpForm.getCode())) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        return merchantLevelService.menuPersionAll(mpForm.getCode());
    }



    /**
     * 根据菜单拿对应权限
     */
    @PostMapping("/getPermissionByMenuIds")
    public Response getPermissionByMenuIds(@RequestBody PerForm perForm) {

        if (StringUtils.isEmpty(perForm.getMenuIds())) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        List<String> menuList = Arrays.asList(perForm.getMenuIds().split(","));
        if (menuList.size()<=0) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        return Response.returnSuccess(merchantLevelService.getPermissionInMenuIds(menuList));
    }


    /**
     * 添加菜单和权限
     *
     * @param roleMenuForm
     * @return
     */
    @AuthSeaMoonKey("商户管理/权限分配")
    @PostMapping("/addRoleMenu")
    public Response addRoleMenu(String tokenCode, HttpServletRequest request, @Validated @RequestBody RoleMenuForm roleMenuForm, BindingResult bindingResult) {

        try {
            if (bindingResult.hasErrors()) {
                return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
            }
            // 菜单
            List<String> menuIdList = Arrays.asList(roleMenuForm.getMenuId().split(","));
            if (menuIdList.size() <= 0) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }

            // 角色
            List<String> PermissionIdList = Arrays.asList(roleMenuForm.getPermissionId().split(","));

            String agentLevel = roleMenuForm.getAgentLevel();

            if (PermissionIdList.size() <= 0) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }

            return merchantLevelService.addRoleMenu(roleMenuForm.getCode(), menuIdList,PermissionIdList,agentLevel,request);
        } catch (Exception e) {
            log.error("manage-->addRoleMenu:{}",e);
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
    }
}
