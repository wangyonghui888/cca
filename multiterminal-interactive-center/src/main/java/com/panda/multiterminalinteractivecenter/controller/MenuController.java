package com.panda.multiterminalinteractivecenter.controller;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.entity.Permissions;
import com.panda.multiterminalinteractivecenter.service.impl.MenuServiceImpl;
import com.panda.multiterminalinteractivecenter.vo.PermissionsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuServiceImpl menuService;

    @PostMapping(value = "/queryMenuList")
    public APIResponse<Object> queryUserList(@RequestBody PermissionsVo permissionsVo){
        return menuService.queryMenuList(permissionsVo);
    }

    @GetMapping(value = "/queryMenuTreeList")
    public APIResponse<Object> queryMenuTreeList(){
        return menuService.queryMenuTreeList();
    }


    @PostMapping(value = "/addMenu")
    public APIResponse<Object> addMenu(HttpServletRequest request, @RequestBody Permissions permissions){
        if (StringUtils.isEmpty(permissions.getUrl()) || StringUtils.isEmpty(permissions.getName())){
            return APIResponse.returnFail("参数异常！");
        }
        if (permissions.getSort() == null){
            permissions.setSort(99L);
        }
        menuService.addPermissions(permissions, request);
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "/updateMenu")
    public APIResponse<Object> updateMenu(HttpServletRequest request, @RequestBody Permissions permissions){
        if (permissions.getId() == null){
            return APIResponse.returnFail("参数异常！");
        }
        if (permissions.getSort() == null){
            permissions.setSort(99L);
        }
        menuService.updatePermissions(permissions, request);
        return APIResponse.returnSuccess();
    }


    @PostMapping(value = "/deleteMenu")
    public APIResponse<Object> deleteMenu(HttpServletRequest request, @RequestBody Permissions permissions){
        if (permissions.getId() == null){
            return APIResponse.returnFail("参数异常！");
        }
        menuService.deletePermissions(permissions, request);
        return APIResponse.returnSuccess();
    }
}
