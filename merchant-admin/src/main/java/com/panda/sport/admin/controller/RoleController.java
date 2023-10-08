package com.panda.sport.admin.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.impl.AdminRoleServiceImpl;
import com.panda.sport.admin.service.impl.AdminRolesMenusServiceImpl;
import com.panda.sport.admin.service.impl.AdminRolesPermissionsServiceImpl;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.admin.vo.form.AdminRoleForm;
import com.panda.sport.admin.vo.form.RoleMenuForm;
import com.panda.sport.admin.vo.form.RolePermissionForm;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.merchant.AdminRole;
import com.panda.sport.merchant.common.po.merchant.AdminRolesMenus;
import com.panda.sport.merchant.common.po.merchant.AdminRolesPermissions;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.AdminRoleVO;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @auth: YK
 * @Description:后台用户角色控制器
 * @Date:2020/5/10 11:58
 */
@Slf4j
@RestController
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    private AdminRoleServiceImpl adminRoleService;

    @Autowired
    private AdminRolesMenusServiceImpl adminRolesMenusService;

    @Autowired
    private AdminRolesPermissionsServiceImpl adminRolesPermissionsService;

    @Autowired
    private MerchantLogService merchantLogService;

    /**
     * 角色列表
     *
     * @return
     */
    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('role')")
    public Response list(@RequestParam(name = "name", defaultValue = "") String name,
                         @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize, true);
        QueryWrapper<AdminRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.eq("merchant_code", SecurityUtils.getUser().getMerchantCode());
        queryWrapper.orderByDesc("create_time");
        List<AdminRole> AdminRoleList = adminRoleService.list(queryWrapper);
        PageInfo<AdminRole> pageInfo = new PageInfo<>(AdminRoleList);
        return Response.returnSuccess(pageInfo);
    }

    /**
     * 获取所有权限
     *
     * @return
     */
    @PostMapping("/getAll")
    public Response getAll() {
        QueryWrapper<AdminRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("merchant_code", SecurityUtils.getUser().getMerchantCode());
        queryWrapper.orderByDesc("create_time");
        List<AdminRole> AdminRoleList = adminRoleService.list(queryWrapper);
        return Response.returnSuccess(AdminRoleList);
    }

    /**
     * 添加
     *
     * @return
     */
    @PostMapping("/add")
    public Response add(HttpServletRequest request, @Validated @RequestBody AdminRoleForm adminRoleForm, BindingResult bindingResult) {
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        if (bindingResult.hasErrors()) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID, bindingResult.getFieldError().getDefaultMessage());
        }
        try {

            //验证角色名称
            AdminRole adminRoleResult = adminRoleService.getOne(
                    new QueryWrapper<AdminRole>()
                            .eq("name", adminRoleForm.getName())
                            .eq("merchant_code", SecurityUtils.getUser().getMerchantCode()));

            if (!StringUtils.isEmpty(adminRoleResult)) {
                return Response.returnFail("该角色名称已经存在");
            }

            AdminRole adminRole = new AdminRole();
            BeanUtils.copyProperties(adminRoleForm, adminRole);
            adminRole.setCreateTime(DateUtil.now());
            adminRole.setCreateUsername(SecurityUtils.getUser().getUsername());
            adminRole.setMerchantCode(SecurityUtils.getUser().getMerchantCode());
            adminRoleService.save(adminRole);
            //记录日志
            JwtUser user = SecurityUtils.getUser();
            merchantLogService.saveLog(MerchantLogPageEnum.ROLE, MerchantLogTypeEnum.ROLE_ADD, null,
                    MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), user.getMerchantCode(), user.getMerchantName(), user.getMerchantId(), language, IPUtils.getIpAddr(request));
            return Response.returnSuccess("添加成功");
        } catch (Exception e) {
            log.error("添加后台管理用户失败:{}", e.getMessage());
            return Response.returnFail("添加失败");
        }
    }

    /**
     * 编辑
     *
     * @return
     */
    @PostMapping("/editPost")
    public Response editPost(HttpServletRequest request, @Validated @RequestBody AdminRoleForm adminRoleForm, BindingResult bindingResult) {
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        if (StringUtils.isEmpty(adminRoleForm.getId())) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        AdminRole adminRoleResult = adminRoleService.getById(adminRoleForm.getId());
        if (StringUtils.isEmpty(adminRoleResult)) {
            return Response.returnFail("用户不存在");
        }
        AdminRole adminRole = new AdminRole();
        BeanUtils.copyProperties(adminRoleForm, adminRole);
        try {
            adminRoleService.updateById(adminRole);
            AdminRoleVO vo1 = new AdminRoleVO();
            BeanUtils.copyProperties(adminRole, vo1);
            AdminRoleVO vo2 = new AdminRoleVO();
            BeanUtils.copyProperties(adminRoleResult, vo2);
            //记录日志
            JwtUser user = SecurityUtils.getUser();
            MerchantFieldUtil fieldUtil = new MerchantFieldUtil<AdminRoleVO>();
            MerchantLogFiledVO filedVO = fieldUtil.compareObject(vo2, vo1);
            merchantLogService.saveLog(MerchantLogPageEnum.ROLE, MerchantLogTypeEnum.ROLE_EDIT, filedVO,
                    MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), user.getMerchantCode(), user.getMerchantName(), adminRoleResult.getId().toString(), language,IPUtils.getIpAddr(request));
            return Response.returnSuccess("更新成功");
        } catch (Exception e) {
            log.error("更新角色失败:{}", e.getMessage());
            return Response.returnFail("更新失败");
        }
    }


    /**
     * 删除
     *
     * @return
     */
    @PostMapping("/delete")
    public Response delete(HttpServletRequest request, Integer id) {
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        boolean isDelete = adminRoleService.removeById(id);
        if (isDelete) {
            //记录日志
            JwtUser user = SecurityUtils.getUser();
            merchantLogService.saveLog(MerchantLogPageEnum.ROLE, MerchantLogTypeEnum.ROLE_DELETE, null,
                    MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), user.getMerchantCode(), user.getMerchantName(), id.toString(), language,IPUtils.getIpAddr(request));
            return Response.returnSuccess();
        }
        return Response.returnFail("删除失败");
    }


    /**
     * 角色添加菜单 和 权限
     *
     * @return
     */
    @PostMapping("/addRoleMenu")
    @Transactional(rollbackFor = Exception.class)
    public Response addRoleMenu(HttpServletRequest request, @Validated @RequestBody RoleMenuForm roleMenuForm, BindingResult bindingResult) {
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }

        // 菜单
        List<String> menuIdList = Arrays.asList(roleMenuForm.getMenuId().split(","));
        if (menuIdList.size() <= 0) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        List<AdminRolesMenus> adminRolesMenusList = new ArrayList<>();
        menuIdList.forEach(e -> {
            AdminRolesMenus adminRolesMenus = new AdminRolesMenus();
            adminRolesMenus.setRoleId(roleMenuForm.getRoleId());
            adminRolesMenus.setMenuId(Long.valueOf(e));
            adminRolesMenusList.add(adminRolesMenus);
        });

        // 角色
        List<String> PermissionIdList = Arrays.asList(roleMenuForm.getPermissionId().split(","));
        if (PermissionIdList.size() <= 0) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        List<AdminRolesPermissions> adminRolesPermissionsList = new ArrayList<>();
        PermissionIdList.forEach(p -> {
            AdminRolesPermissions adminRolesPermissions = new AdminRolesPermissions();
            adminRolesPermissions.setRoleId(roleMenuForm.getRoleId());
            adminRolesPermissions.setPermissionId(Long.valueOf(p));

            adminRolesPermissionsList.add(adminRolesPermissions);
        });


        AdminRole adminRole = new AdminRole();
        adminRole.setId(roleMenuForm.getRoleId());
        adminRole.setAuthorizedTime(DateUtil.now());
        try {
            adminRoleService.updateById(adminRole);
            adminRolesMenusService.remove(new QueryWrapper<AdminRolesMenus>().eq("role_id", roleMenuForm.getRoleId()));
            adminRolesMenusService.saveBatch(adminRolesMenusList);
            List<AdminRolesPermissions> list = adminRolesPermissionsService.list(new QueryWrapper<AdminRolesPermissions>().eq("role_id", roleMenuForm.getRoleId()));
            adminRolesPermissionsService.remove(new QueryWrapper<AdminRolesPermissions>().eq("role_id", roleMenuForm.getRoleId()));
            adminRolesPermissionsService.saveBatch(adminRolesPermissionsList);
            //记录日志
            JwtUser user = SecurityUtils.getUser();
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("roleId"));
            filedVO.getBeforeValues().add(list == null || list.size() == 0 ? "" : JSON.toJSONString(list));
            filedVO.getAfterValues().add(JSON.toJSONString(adminRolesPermissionsList));
            merchantLogService.saveLog(MerchantLogPageEnum.ROLE, MerchantLogTypeEnum.ROLE_SET, filedVO,
                    MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), user.getMerchantCode(),
                    user.getMerchantName(), roleMenuForm.getRoleId().toString(), language,IPUtils.getIpAddr(request));
            return Response.returnSuccess("添加成功");
        } catch (Exception e) {
            log.error("更新用户角色失败:{}", e.getMessage());
            return Response.returnFail("添加失败");
        }
    }


    /**
     * 获取该角色下的菜单
     *
     * @param roleId
     * @return
     */
    @PostMapping("/getMenuByRoleId")
    public Response getMenuByRoleId(Integer roleId) {

        if (StringUtils.isEmpty(roleId)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        // 角色所对应的菜单
        QueryWrapper<AdminRolesMenus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<AdminRolesMenus> adminRolesMenusList = adminRolesMenusService.list(queryWrapper);
        List<String> menuId = adminRolesMenusList.stream().map(e -> String.valueOf(e.getMenuId())).collect(Collectors.toList());
        String menuIdString = String.join(",", menuId);

        // 角色所对应的权限
        QueryWrapper<AdminRolesPermissions> rolesPermissionsQueryWrapper = new QueryWrapper<>();
        rolesPermissionsQueryWrapper.eq("role_id", roleId);
        List<AdminRolesPermissions> adminRolesPermissionsList = adminRolesPermissionsService.list(rolesPermissionsQueryWrapper);
        List<String> permissionId = adminRolesPermissionsList.stream().map(e -> String.valueOf(e.getPermissionId())).collect(Collectors.toList());
        String permissionIdString = String.join(",", permissionId);

        // 返回值
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("menuIds", menuIdString);
            put("permissionIds", permissionIdString);
        }};
        return Response.returnSuccess(map);
    }


    /**
     * 删除角色菜单
     *
     * @return
     */
    @PostMapping(value = "/deleteRoleMenu")
    public Response deleteRoleMenu(
            @RequestParam(name = "roleId") Integer roleId,
            @RequestParam(name = "menuId") Integer menuId
    ) {

        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(menuId)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        QueryWrapper<AdminRolesMenus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        queryWrapper.eq("menu_id", menuId);
        try {
            adminRolesMenusService.remove(queryWrapper);
            return Response.returnSuccess("删除成功");
        } catch (Exception e) {
            log.error("删除角色菜单失败:{}", e.getMessage());
            return Response.returnFail("删除失败");
        }
    }


    /**
     * 角色添加权限
     *
     * @return
     */
    @PostMapping(value = "/addRolePermission")
    @Transactional(rollbackFor = Exception.class)
    public Response addRolePermission(@Validated @RequestBody RolePermissionForm rolePermissionForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        List<String> PermissionIdList = Arrays.asList(rolePermissionForm.getPermissionId().split(","));
        if (PermissionIdList.size() <= 0) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        List<AdminRolesPermissions> adminRolesPermissionsList = new ArrayList<>();
        PermissionIdList.forEach(p -> {
            AdminRolesPermissions adminRolesPermissions = new AdminRolesPermissions();
            adminRolesPermissions.setRoleId(rolePermissionForm.getRoleId());
            adminRolesPermissions.setPermissionId(Long.valueOf(p));

            adminRolesPermissionsList.add(adminRolesPermissions);
        });
        try {
            adminRolesPermissionsService.remove(new QueryWrapper<AdminRolesPermissions>().eq("role_id", rolePermissionForm.getRoleId()));
            adminRolesPermissionsService.saveBatch(adminRolesPermissionsList);
            return Response.returnSuccess("添加成功");
        } catch (Exception e) {
            log.error("更新用户角色权限失败:{}", e.getMessage());
            return Response.returnFail("添加失败");
        }

    }

    /**
     * 删除用户角色
     *
     * @return
     */
    @PostMapping(value = "/deleteRolePermission")
    public Response deleteRolePermission(
            @RequestParam(name = "roleId") Integer roleId,
            @RequestParam(name = "permissionId") Integer permissionId
    ) {
        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(permissionId)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        QueryWrapper<AdminRolesPermissions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        queryWrapper.eq("permission_id", permissionId);
        try {
            adminRolesPermissionsService.remove(queryWrapper);
            return Response.returnSuccess("删除成功");
        } catch (Exception e) {
            log.error("删除角色菜单失败:{}", e.getMessage());
            return Response.returnFail("删除失败");
        }
    }

}
