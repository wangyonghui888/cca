package com.panda.sport.bc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.bc.service.impl.BcAdminRoleServiceImpl;
import com.panda.sport.merchant.common.po.bcorder.BcAdminRole;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName RoleController
 * @auth YK
 * @Description
 * @Date 2020-09-11 17:00
 * @Version
 */
@Slf4j
@RestController
@RequestMapping("/bc/role")
public class RoleController {

    @Autowired
    private BcAdminRoleServiceImpl adminRoleService;

    /**
    * @description: 角色列表
    * @Param: [name, pageNum, pageSize]
    * @return: com.panda.sport.merchant.common.vo.Response
    * @author: YK
    * @date: 2020/9/11 17:02
    */
    @PostMapping("/list")
    public Response list(@RequestParam(name = "name",defaultValue = "") String name,
                         @RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize",defaultValue = "20") Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize,true);
        QueryWrapper<BcAdminRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        queryWrapper.orderByDesc("create_time");
        List<BcAdminRole> AdminRoleList = adminRoleService.list(queryWrapper);
        PageInfo<BcAdminRole> pageInfo = new PageInfo<>(AdminRoleList);
        return Response.returnSuccess(pageInfo);
    }


    /**
     * 获取所有权限
     * @return
     */
    @PostMapping("/getAll")
    public Response getAll() {
        QueryWrapper<BcAdminRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<BcAdminRole> AdminRoleList = adminRoleService.list(queryWrapper);
        return Response.returnSuccess(AdminRoleList);
    }



}
