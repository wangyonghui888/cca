package com.panda.sport.bc.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.bc.service.impl.BcAdminMenuServiceImpl;
import com.panda.sport.bc.utils.MenuUtils;
import com.panda.sport.bc.vo.BcAdminMenuVo;
import com.panda.sport.bc.vo.form.BcAdminMenuForm;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bcorder.BcAdminMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.panda.sport.merchant.common.vo.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName MenuController
 * @auth YK
 * @Description : 菜单栏
 * @Date 2020-09-03 11:59
 * @Version
 */
@Slf4j
@RestController
@RequestMapping("/bc/menu")
public class MenuController {

    @Autowired
    private BcAdminMenuServiceImpl adminMenuService;

    /**
    * @description: 菜单列表
    * @Param: [name, pgNum, pgSize]
    * @return: com.panda.sport.merchant.common.vo.Response
    * @author: YK
    * @date: 2020/9/11 12:03
    */
    @PostMapping("/list")
    public Response list(@RequestParam(name = "name", defaultValue = "") String name,
                         @RequestParam(name = "pgNum", defaultValue = "1") Integer pgNum,
                         @RequestParam(name = "pgSize", defaultValue = "10") Integer pgSize) {

        PageHelper.startPage(pgNum, pgSize, true);
        QueryWrapper<BcAdminMenu> queryWrapper = new QueryWrapper();
        queryWrapper.like("name", name);
        List<BcAdminMenu> AdminMenuList = adminMenuService.list(queryWrapper);
        PageInfo<BcAdminMenu> pageInfo = new PageInfo<>(AdminMenuList);
        return Response.returnSuccess(pageInfo);
    }


    /**
    * @description:  获取所有菜单
    * @Param: []
    * @return: com.panda.sport.merchant.common.vo.Response
    * @author: YK
    * @date: 2020/9/11 12:03
    */
    @PostMapping("/getAll")
    public Response getAll() {

        QueryWrapper<BcAdminMenu> queryWrapper = new QueryWrapper();
        queryWrapper.orderByAsc("id");
        List<BcAdminMenu> adminMenuList = adminMenuService.list();
        List<BcAdminMenuVo> adminMenuVoList = new ArrayList<>();

        for (BcAdminMenu adminMenu : adminMenuList) {
            BcAdminMenuVo adminMenuVo = new BcAdminMenuVo();
            BeanUtils.copyProperties(adminMenu, adminMenuVo);
            adminMenuVoList.add(adminMenuVo);
        }

        List<BcAdminMenuVo> rootMenu = new ArrayList<>();
        for (BcAdminMenuVo adminMenuVo : adminMenuVoList) {
            if (adminMenuVo.getPid() == 0) {
                adminMenuVo.setParentName("");
                rootMenu.add(adminMenuVo);
            }
        }
        for (BcAdminMenuVo amv : rootMenu) {
            List<BcAdminMenuVo> childList = MenuUtils.getChild(amv, adminMenuVoList);
            amv.setChildren(childList);
        }
        return Response.returnSuccess(rootMenu);
    }


    /**
    * @description: 添加
    * @Param: [adminMenuForm, bindingResult]
    * @return: com.panda.sport.merchant.common.vo.Response
    * @author: YK
    * @date: 2020/9/11 12:03
    */
    @PostMapping("/add")
    public Response add(@Validated @RequestBody BcAdminMenuForm adminMenuForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID, bindingResult.getFieldError().getDefaultMessage());
        }
        BcAdminMenu adminMenu = new BcAdminMenu();
        BeanUtils.copyProperties(adminMenuForm, adminMenu);
        adminMenu.setSort(StringUtils.isEmpty(adminMenuForm.getSort()) ? 50 : adminMenuForm.getSort());
        adminMenu.setCreateTime(DateUtil.now());
        try {
            adminMenuService.save(adminMenu);
            return Response.returnSuccess("添加成功");
        } catch (Exception e) {
            log.error("添加后台失败:{}", e.getMessage());
            return Response.returnFail("添加失败");
        }
    }


    /**
    * @description: 编辑
    * @Param: [adminMenuForm, bindingResult]
    * @return: com.panda.sport.merchant.common.vo.Response
    * @author: YK
    * @date: 2020/9/11 12:04
    */
    @PostMapping("/editPost")
    public Response editPost(@Validated @RequestBody BcAdminMenuForm adminMenuForm, BindingResult bindingResult) {

        if (StringUtils.isEmpty(adminMenuForm.getId())) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        BcAdminMenu adminMenuResult = adminMenuService.getById(adminMenuForm.getId());
        if (StringUtils.isEmpty(adminMenuResult)) {
            return Response.returnFail("菜单不存在");
        }
        BcAdminMenu adminMenu = new BcAdminMenu();
        BeanUtils.copyProperties(adminMenuForm, adminMenu);
        try {
            adminMenuService.updateById(adminMenu);
            return Response.returnSuccess("更新成功");
        } catch (Exception e) {
            log.error("更新菜单失败:{}", e.getMessage());
            return Response.returnFail("更新失败");
        }
    }

    /**
    * @description: 删除
    * @Param: [id]
    * @return: com.panda.sport.merchant.common.vo.Response
    * @author: YK
    * @date: 2020/9/11 12:04
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
    * @description:   根据菜单获取获取菜单所对应的权限
    * @Param: [menuIds]
    * @return: com.panda.sport.merchant.common.vo.Response
    * @author: YK
    * @date: 2020/9/11 12:04
    */
    @PostMapping("/getPermissionByMenuIds")
    public Response getPermissionByMenuIds(String menuIds) {

        if (StringUtils.isEmpty(menuIds)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        List<String> menuList = Arrays.asList(menuIds.split(","));
        if (menuList.size() <= 0) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        return Response.returnSuccess(adminMenuService.getPermissionInMenuIds(menuList));
    }

}
