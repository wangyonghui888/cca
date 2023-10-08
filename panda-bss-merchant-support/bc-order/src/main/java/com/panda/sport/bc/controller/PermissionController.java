package com.panda.sport.bc.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.bc.service.impl.BcAdminPermissionServiceImpl;
import com.panda.sport.bc.vo.BcAdminPermissionVo;
import com.panda.sport.bc.vo.form.BcAdminPermissionForm;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bcorder.BcAdminPermission;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PermissionController
 * @auth YK
 * @Description 权限列表
 * @Date 2020-09-03 13:31
 * @Version
 */
@Slf4j
@RestController
@RequestMapping("/bc/permission")
public class PermissionController {

    @Autowired
    private BcAdminPermissionServiceImpl adminPermissionService;

    /**
    * @description:  权限列表
    * @Param: [name, pgNum, pgSize]
    * @return: com.panda.sport.merchant.common.vo.Response
    * @author: YK
    * @date: 2020/9/11 12:02
    */
    @PostMapping("/list")
    public Response list(@RequestParam(name = "name", defaultValue = "") String name,
                         @RequestParam(name = "pgNum", defaultValue = "1") Integer pgNum,
                         @RequestParam(name = "pgSize", defaultValue = "20") Integer pgSize) {

        PageHelper.startPage(pgNum, pgSize, true);
        QueryWrapper<BcAdminPermission> queryWrapper = new QueryWrapper();
        queryWrapper.like("name", name);
        List<BcAdminPermission> AdminMenuList = adminPermissionService.list(queryWrapper);
        PageInfo<BcAdminPermission> pageInfo = new PageInfo<>(AdminMenuList);
        return Response.returnSuccess(pageInfo);
    }


   /**
   * @description:  获取所有权限
   * @Param: []
   * @return: com.panda.sport.merchant.common.vo.Response
   * @author: YK
   * @date: 2020/9/11 12:02
   */
    @PostMapping("/getAll")
    public Response getAll() {

        // 去菜单大于0
        List<BcAdminPermission> adminPermissionList = adminPermissionService.list(new QueryWrapper<BcAdminPermission>().gt("pid", 0));
        List<BcAdminPermissionVo> adminPermissionVoList = new ArrayList<>();
        for (BcAdminPermission adminPermission : adminPermissionList) {
            BcAdminPermissionVo adminPermissionVo = new BcAdminPermissionVo();
            BeanUtils.copyProperties(adminPermission, adminPermissionVo);
            adminPermissionVoList.add(adminPermissionVo);
        }
        return Response.returnSuccess(adminPermissionVoList);
    }

    /**
    * @description: 添加
    * @Param: [adminPermissionForm, bindingResult]
    * @return: com.panda.sport.merchant.common.vo.Response
    * @author: YK
    * @date: 2020/9/11 12:02
    */
    @PostMapping("/add")
    public Response add(@Validated @RequestBody BcAdminPermissionForm adminPermissionForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID, bindingResult.getFieldError().getDefaultMessage());
        }
        BcAdminPermission adminPermission = new BcAdminPermission();
        BeanUtils.copyProperties(adminPermissionForm, adminPermission);
        adminPermission.setCreateTime(DateUtil.now());
        try {
            adminPermissionService.save(adminPermission);
            return Response.returnSuccess("添加成功");
        } catch (Exception e) {
            log.error("添加后台失败:{}", e.getMessage());
            return Response.returnFail("添加失败");
        }
    }

    /**
    * @description: 编辑
    * @Param: [adminPermissionForm, bindingResult]
    * @return: com.panda.sport.merchant.common.vo.Response
    * @author: YK
    * @date: 2020/9/11 12:02
    */
    @PostMapping("/editPost")
    public Response editPost(@Validated @RequestBody BcAdminPermissionForm adminPermissionForm, BindingResult bindingResult) {

        if (StringUtils.isEmpty(adminPermissionForm.getId())) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        BcAdminPermission adminPermissionResult = adminPermissionService.getById(adminPermissionForm.getId());
        if (StringUtils.isEmpty(adminPermissionResult)) {
            return Response.returnFail("菜单不存在");
        }
        BcAdminPermission adminPermission = new BcAdminPermission();
        BeanUtils.copyProperties(adminPermissionForm, adminPermission);
        try {
            adminPermissionService.updateById(adminPermission);
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
    * @date: 2020/9/11 12:02
    */
    @PostMapping("/delete")
    public Response delete(Integer id) {
        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        boolean isDelete = adminPermissionService.removeById(id);
        if (isDelete) {
            return Response.returnSuccess();
        }
        return Response.returnFail("删除失败");
    }

}
