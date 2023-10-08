package com.panda.sport.admin.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.admin.service.impl.AdminMenuServiceImpl;
import com.panda.sport.admin.service.impl.AdminPermissionServiceImpl;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.admin.vo.AdminPermissionVo;
import com.panda.sport.admin.vo.form.AdminPermissionForm;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.merchant.AdminPermission;
import com.panda.sport.merchant.common.po.merchant.AdminRole;
import com.panda.sport.merchant.common.po.merchant.AdminRolesPermissions;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auth: YK
 * @Description:权限列表
 * @Date:2020/5/14 17:52
 */
@Slf4j
@RestController
@RequestMapping("/admin/permission")
public class PermissionController {


    @Autowired
    private AdminPermissionServiceImpl adminPermissionService;

    @Autowired
    private AdminMenuServiceImpl adminMenuService;

    @Autowired
    private AdminMenuMapper adminMenuMapper;

    /**
     * 权限列表
     * @return
     */
    @PostMapping("/list")
    public Response list(@RequestParam(name = "name",defaultValue = "") String name,
                         @RequestParam(name = "pgNum",defaultValue = "1") Integer pgNum,
                         @RequestParam(name = "pgSize",defaultValue = "20") Integer pgSize) {

        PageHelper.startPage(pgNum, pgSize,true);
        QueryWrapper<AdminPermission> queryWrapper = new QueryWrapper();
        queryWrapper.like("name",name);
        List<AdminPermission> AdminMenuList = adminPermissionService.list(queryWrapper);
        PageInfo<AdminPermission> pageInfo = new PageInfo<>(AdminMenuList);
        return Response.returnSuccess(pageInfo);
    }


    /**
     * 获取所有权限
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

        List<AdminRolesPermissions> adminRolesPermissionsList = adminMenuMapper.getRolesPermissions(roleId);
        List<Long> permissionIdList = adminRolesPermissionsList.stream().map(e -> e.getPermissionId()).collect(Collectors.toList());

        // 去菜单大于0
        List<AdminPermission> adminPermissionList = adminPermissionService.list(new QueryWrapper<AdminPermission>().in("id",permissionIdList));
        List<AdminPermissionVo> adminPermissionVoList = new ArrayList<>();
        Integer agentLevel = SecurityUtils.getUser().getAgentLevel();

        String merchantCreditCode = adminMenuService.getMerchantCredit(SecurityUtils.getUser().getMerchantCode());

        for (AdminPermission adminPermission : adminPermissionList) {

            /*
            if (agentLevel !=1 && "二级商户管理".equals(adminPermission.getAlias())) {
                continue;
            }

            if(agentLevel == 2 && "电子账单".equals(adminPermission.getAlias())){
                continue;
            }

            // 只有代理商才有渠道商户管理
            if (agentLevel != 10 && "渠道商户管理".equals(adminPermission.getAlias())) {
                continue;
            }

            // 代理商账户没有 我的证书 和 操作日志查询
            if (agentLevel == 10) {
                if ( "我的证书".equals(adminPermission.getAlias()) || "操作日志查询".equals(adminPermission.getAlias())) {
                    continue;
                }
            }
            */

            if (StringUtils.isEmpty(merchantCreditCode)) {
                if ("取消注单".equals(adminPermission.getAlias())){
                    continue;
                }
            }

            AdminPermissionVo adminPermissionVo = new AdminPermissionVo();
            BeanUtils.copyProperties(adminPermission,adminPermissionVo);
            adminPermissionVoList.add(adminPermissionVo);
        }
        return Response.returnSuccess(adminPermissionVoList);
    }

    /**
     * 添加
     * @return
     */
    @PostMapping("/add")
    public Response add(@Validated @RequestBody AdminPermissionForm adminPermissionForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID,bindingResult.getFieldError().getDefaultMessage());
        }
        AdminPermission adminPermission = new AdminPermission();
        BeanUtils.copyProperties(adminPermissionForm,adminPermission);
        adminPermission.setCreateTime(DateUtil.now());
        try {
            adminPermissionService.save(adminPermission);
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
    public Response editPost(@Validated  @RequestBody AdminPermissionForm adminPermissionForm, BindingResult bindingResult) {

        if (StringUtils.isEmpty(adminPermissionForm.getId())) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (bindingResult.hasErrors()){
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        AdminPermission adminPermissionResult = adminPermissionService.getById(adminPermissionForm.getId());
        if (StringUtils.isEmpty(adminPermissionResult)) {
            return Response.returnFail("菜单不存在");
        }
        AdminPermission adminPermission = new AdminPermission();
        BeanUtils.copyProperties(adminPermissionForm,adminPermission);
        try {
            adminPermissionService.updateById(adminPermission);
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
        boolean isDelete = adminPermissionService.removeById(id);
        if (isDelete) {
            return Response.returnSuccess();
        }
        return Response.returnFail("删除失败");
    }



}
