package com.panda.sport.admin.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.admin.service.impl.AdminMenuServiceImpl;
import com.panda.sport.admin.utils.MenuUtils;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.admin.vo.AdminMenuVo;
import com.panda.sport.admin.vo.form.AdminMenuForm;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.merchant.AdminMenu;
import com.panda.sport.merchant.common.po.merchant.AdminRole;
import com.panda.sport.merchant.common.po.merchant.AdminRolesMenus;
import com.panda.sport.merchant.common.vo.Response;

import com.panda.sport.merchant.mapper.AdminMenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auth: YK
 * @Description:菜单栏控制器
 * @Date:2020/5/10 11:58
 */
@Slf4j
@RestController
@RequestMapping("/admin/menu")
public class MenuController {

    @Autowired
    private AdminMenuServiceImpl adminMenuService;

    //
    @Autowired
    private AdminMenuMapper adminMenuMapper;

    /**
     * 菜单列表
     * @return
     */
    @PostMapping("/list")
    public Response list(@RequestParam(name = "name",defaultValue = "") String name,
                         @RequestParam(name = "pgNum",defaultValue = "1") Integer pgNum,
                         @RequestParam(name = "pgSize",defaultValue = "10") Integer pgSize) {

        PageHelper.startPage(pgNum, pgSize,true);
        QueryWrapper<AdminMenu> queryWrapper = new QueryWrapper();
        queryWrapper.like("name",name);
        List<AdminMenu> AdminMenuList = adminMenuService.list(queryWrapper);
        PageInfo<AdminMenu> pageInfo = new PageInfo<>(AdminMenuList);
        return Response.returnSuccess(pageInfo);
    }

    /**
     * 获取所有的菜单
     * @return
     */
    @PostMapping("/getAll")
    public Response getAll() {

        List<AdminRole> adminRoles = adminMenuMapper.getRoleAdmin(SecurityUtils.getUser().getMerchantCode(),1);
        if (CollectionUtil.isEmpty(adminRoles)) {
            return Response.returnFail("请先添加商户管理员账号");
        }

        // 获取所对应的账号
        Long roleId = adminRoles.get(0).getId();
        List<AdminRolesMenus> adminRolesMenusList = adminMenuMapper.getRoleMenu(roleId);
        List<Long> menuId = adminRolesMenusList.stream().map(e -> e.getMenuId()).collect(Collectors.toList());
        List<AdminMenu> adminMenuList = adminMenuService.list(new QueryWrapper<AdminMenu>().in("id",menuId));
        List<AdminMenuVo> adminMenuVoList = new ArrayList<>();

        // 判断是不是信用商户，如果有值就是信用商户
        String merchantCreditCode = adminMenuService.getMerchantCredit(SecurityUtils.getUser().getMerchantCode());
        for (AdminMenu adminMenu :adminMenuList) {

            if (SecurityUtils.getUser().getAgentLevel() !=1 && "二级商户管理".equals(adminMenu.getName())) {
                continue;
            }
            if (StringUtils.isEmpty(merchantCreditCode)) {
                if ("取消注单".equals(adminMenu.getName())){
                    continue;
                }
            }
            AdminMenuVo adminMenuVo = new AdminMenuVo();
            BeanUtils.copyProperties(adminMenu,adminMenuVo);
            adminMenuVoList.add(adminMenuVo);
        }

        List<AdminMenuVo> rootMenu = new ArrayList<>();
        for (AdminMenuVo adminMenuVo : adminMenuVoList) {
            if (adminMenuVo.getPid() ==0 ) {
                adminMenuVo.setParentName("");
                rootMenu.add(adminMenuVo);
            }
        }
        for (AdminMenuVo amv : rootMenu) {
            List<AdminMenuVo> childList = MenuUtils.getChild(amv,adminMenuVoList);
            amv.setChildren(childList);
        }
        return Response.returnSuccess(rootMenu);
    }



    /**
     * 添加
     * @return
     */
    @PostMapping("/add")
    public Response add(@Validated  @RequestBody AdminMenuForm adminMenuForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID,bindingResult.getFieldError().getDefaultMessage());
        }
        AdminMenu adminMenu = new AdminMenu();
        BeanUtils.copyProperties(adminMenuForm,adminMenu);
        adminMenu.setSort(StringUtils.isEmpty(adminMenuForm.getSort()) ? 50 : adminMenuForm.getSort());
        adminMenu.setCreateTime(DateUtil.now());
        try {
            adminMenuService.save(adminMenu);
            return Response.returnSuccess("添加成功");
        } catch (Exception e) {
            log.error("添加后台失败:{}",e.getMessage());
            return Response.returnFail("添加失败");
        }
    }

    /**
     * 编辑
     * @return
     */
    @PostMapping("/editPost")
    public Response editPost(@Validated  @RequestBody AdminMenuForm adminMenuForm, BindingResult bindingResult) {

        if (StringUtils.isEmpty(adminMenuForm.getId())) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (bindingResult.hasErrors()){
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        AdminMenu adminMenuResult = adminMenuService.getById(adminMenuForm.getId());
        if (StringUtils.isEmpty(adminMenuResult)) {
            return Response.returnFail("菜单不存在");
        }
        AdminMenu adminMenu = new AdminMenu();
        BeanUtils.copyProperties(adminMenuForm,adminMenu);
        try {
            adminMenuService.updateById(adminMenu);
            return Response.returnSuccess("更新成功");
        } catch (Exception e) {
            log.error("更新菜单失败:{}",e.getMessage());
            return Response.returnFail("更新失败");
        }
    }

    /**
     * 删除
     * @return
     */
    @PostMapping("/delete")
    public Response delete(Integer id) {
        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        boolean isDelete = adminMenuService.removeById(id);
        if (isDelete) {
            return Response.returnSuccess();
        }
        return Response.returnFail("删除失败");
    }

    /**
     * 根据菜单获取获取菜单所对应的权限
     * @param menuIds
     * @return
     */
    @PostMapping("/getPermissionByMenuIds")
    public Response getPermissionByMenuIds(String menuIds) {

        if (StringUtils.isEmpty(menuIds)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        List<String> menuList = Arrays.asList(menuIds.split(","));
        if (menuList.size()<=0) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        return Response.returnSuccess(adminMenuService.getPermissionInMenuIds(menuList));
    }


    /**
     * 自动分配角色
     * @return
     */
    @PostMapping("/allocatePermisson")
    public Response allocatePermisson() {

        try {
            adminMenuService.allocatePermisson();
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("自动分配菜单角色失败:{}",e);
            return Response.returnFail(e.getMessage());
        }
    }

}
