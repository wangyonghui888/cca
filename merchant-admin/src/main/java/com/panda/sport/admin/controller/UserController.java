package com.panda.sport.admin.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.impl.AdminRoleServiceImpl;
import com.panda.sport.admin.service.impl.AdminUserServiceImpl;
import com.panda.sport.admin.utils.EncryptUtils;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.admin.vo.AdminUserVo;
import com.panda.sport.admin.vo.form.AdminUserForm;
import com.panda.sport.admin.vo.form.EditPwdForm;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.admin.service.ExternalMerchantConfigService;
import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.merchant.AdminRole;
import com.panda.sport.merchant.common.po.merchant.AdminUser;

import com.panda.sport.merchant.common.utils.*;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;

import com.panda.sport.merchant.common.vo.merchant.QueryConditionSettingEditReqVO;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.service.TournamentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
 * @Description:后台用户管理控制器
 * @Date:2020/5/10 11:57
 */
@Slf4j
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private AdminUserServiceImpl adminUserService;

    @Autowired
    private AdminRoleServiceImpl adminRoleService;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private ExternalMerchantConfigService externalMerchantConfigService;

    /**
     * 赛事注单统计
     */
    @GetMapping(value = "/queryTournamentList")
    public Response queryTournamentList(HttpServletRequest request, @RequestParam(value = "args", required = false) String args, @RequestParam(value = "level", required = false) Integer level, @RequestParam(value = "sportId", required = false) Long sportId) {
        try {
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
                language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            }
            return Response.returnSuccess(tournamentService.findTournamentListBySportsLevel(args, language, level , sportId));
        } catch (Exception e) {
            log.error("联赛等级下拉查询失败！", e);
            return Response.returnFail("联赛等级下拉查询失败！");
        }
    }

    /**
     * 用户列表
     *
     * @return
     */
    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('user')")
    public Response list(@RequestParam(name = "username", defaultValue = "") String username,
                         @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                         @RequestParam(name = "enabled", defaultValue = "2") Integer enabled,
                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, true);
        QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        if (enabled != 2) {
            // 传2 是所有
            queryWrapper.eq("enabled", enabled);
        }
        queryWrapper.eq("merchant_code", SecurityUtils.getUser().getMerchantCode());
        queryWrapper.orderByDesc("create_time");
        List<AdminUser> AdminUserList = adminUserService.list(queryWrapper);
        List<Long> roleIdS = AdminUserList.stream().map(AdminUser::getRoleId).collect(Collectors.toList());
        Map<Long, String> roleMap = Collections.EMPTY_MAP;
        if (roleIdS.size() > 0) {
            List<AdminRole> adminRoleList = adminRoleService.list(new QueryWrapper<AdminRole>().in("id", roleIdS));
            roleMap = adminRoleList.stream().collect(Collectors.toMap(AdminRole::getId, AdminRole::getName));
        }
        List<AdminUserVo> adminUserVoList = new ArrayList<>();
        for (AdminUser a : AdminUserList) {
            AdminUserVo adminUserVo = new AdminUserVo();
            BeanUtils.copyProperties(a, adminUserVo);
            adminUserVo.setRoleName(roleMap.containsKey(a.getRoleId()) ? roleMap.get(a.getRoleId()) : "");
            adminUserVoList.add(adminUserVo);
        }
        PageInfo<AdminUserVo> pageInfo = new PageInfo<>(adminUserVoList);
        return Response.returnSuccess(pageInfo);
    }



    /**
     * 添加
     *
     * @return
     */
    @PostMapping("/add")
    public Response add(HttpServletRequest request, @Validated @RequestBody AdminUserForm adminUserForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID, bindingResult.getFieldError().getDefaultMessage());
        }
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        JwtUser user = SecurityUtils.getUser();

        //2058需求，增加密码复杂程度，随机生成至少12位且必须含特殊字符+大小字母+数字
//        adminUserForm.setPassword(adminUserForm.getUsername() + user.getMerchantCode());
        String psw=CreatPswUtil.getPsw(12);//生成随机密码
        adminUserForm.setPassword(psw);//保存MD5加密码

        AdminUser adminUserResult = adminUserService.getOne(new QueryWrapper<AdminUser>().eq("username", adminUserForm.getUsername()));
        if (!StringUtils.isEmpty(adminUserResult)) {
            log.info("UserController.add" + adminUserResult.getUsername());
            return Response.returnFail("该用户名已经存在");
        }

        AdminUser adminUser = new AdminUser();
        BeanUtils.copyProperties(adminUserForm, adminUser);
        adminUser.setId(UUIDUtils.getUUID());
        adminUser.setMerchantId(user.getMerchantId());
        adminUser.setMerchantCode(user.getMerchantCode());
        adminUser.setMerchantName(user.getMerchantName());
        adminUser.setParentMerchantCode(user.getParentMerchantCode());
        adminUser.setAgentLevel(user.getAgentLevel());

        adminUser.setPassword(EncryptUtils.encryptPassword(adminUserForm.getPassword()));
        adminUser.setPswCode(psw);
        adminUser.setCreateTime(DateUtil.now());
        adminUser.setLastPasswordResetTime(DateUtil.now());
        try {
            adminUserService.save(adminUser);
            //记录日志
            merchantLogService.saveLog(MerchantLogPageEnum.USER, MerchantLogTypeEnum.USER_MANAGER_ADD, null,
                    MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), user.getMerchantCode(), user.getMerchantName(), adminUser.getMerchantId(), language, IPUtils.getIpAddr(request));
//            return Response.returnSuccess("保存成功,pswCode:"+psw);

            return Response.returnSuccess(adminUser);
        } catch (Exception e) {
            log.error("添加后台管理用户失败:{}", e.getMessage());
            return Response.returnFail("添加失败");
        }
    }

    /**
     * 添加
     *
     * @return
     */
    @PostMapping("/restPwd")
    public Response restPwd(HttpServletRequest request, @Param("username") String username) {
        if (StringUtils.isEmpty(username)) {
            return Response.returnFail("用户名不能为空！");
        }
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        JwtUser user = SecurityUtils.getUser();
        //2058需求，增加密码复杂程度，随机生成至少12位且必须含特殊字符+大小字母+数字
        String pwd= CreatPswUtil.getPsw(12);//生成随机密码
//        String pwd = username + user.getMerchantCode();
        AdminUser adminUserResult = adminUserService.getOne(new QueryWrapper<AdminUser>().eq("username", username));
        if (adminUserResult == null) {
            return Response.returnFail("该用户名不存在");
        }
        try {
            UpdateWrapper<AdminUser> updateWrapper = new UpdateWrapper<AdminUser>();
            updateWrapper.eq("username", username);
            updateWrapper.set("password", com.panda.sport.merchant.common.utils.EncryptUtils.encryptMd5(pwd));
            updateWrapper.set("psw_code",pwd);//修改密码明码

            adminUserService.update(updateWrapper);
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            List<String> list = new ArrayList<>();
            list.add("password");
            filedVO.setFieldName(list);
            List<String> pwdlist = new ArrayList<>();
            pwdlist.add(pwd);
            filedVO.setAfterValues(pwdlist);

            List<String> beforepwdlist = new ArrayList<>();
            beforepwdlist.add(adminUserResult.getPassword());
            filedVO.setBeforeValues(beforepwdlist);
            //记录日志
            merchantLogService.saveLog(MerchantLogPageEnum.USER, MerchantLogTypeEnum.USER_PASSWORD_UPDATE, filedVO,
                    MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), user.getMerchantCode(), user.getMerchantName(), user.getMerchantId(), language,IPUtils.getIpAddr(request));
//            return Response.returnSuccess("保存成功,pswCode:"+pwd);
            adminUserResult.setPassword(com.panda.sport.merchant.common.utils.EncryptUtils.encryptMd5(pwd));
            adminUserResult.setPswCode(pwd);
            return Response.returnSuccess(adminUserResult);
        } catch (Exception e) {
            log.error("添加后台管理用户失败", e);
            return Response.returnFail("添加失败");
        }
    }

    /**
     * 编辑
     *
     * @return
     */
    @PostMapping("/editPost")
    @Transactional(rollbackFor = Exception.class)
    public Response editPost(HttpServletRequest request, @Validated @RequestBody AdminUserForm adminUserForm, BindingResult bindingResult) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        if (StringUtils.isEmpty(adminUserForm.getId())) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        AdminUser adminUserResult = adminUserService.getById(adminUserForm.getId());
        if (StringUtils.isEmpty(adminUserResult)) {
            return Response.returnFail("用户不存在");
        }
        AdminUser adminUserResultTwo = adminUserService.getOne(new QueryWrapper<AdminUser>().eq("username", adminUserForm.getUsername()));
        if (!StringUtils.isEmpty(adminUserResultTwo)) {
            if (!adminUserResultTwo.getId().equals(adminUserForm.getId())) {
                return Response.returnFail("该用户名已经存在");
            }
        }
        if (Objects.equals(adminUserResult.getIsAdmin(), CommonDefaultValue.DifferentiateStatus.YES)) {
            //如果是超级管理员，名字不能修改只能修改密码
            adminUserForm.setUsername(adminUserResult.getUsername());
        }
        boolean isUpdata = false;
        AdminUser adminUser = new AdminUser();
        BeanUtils.copyProperties(adminUserForm, adminUser);
        if (!StringUtils.isEmpty(adminUserForm.getPassword()) && !Objects.equals(adminUserForm.getPassword(),adminUserResult.getPassword())) {
            // 如果编辑修改了密码
            adminUser.setPassword(EncryptUtils.encryptPassword(adminUserForm.getPassword()));
            adminUser.setPswCode(adminUserForm.getPassword());
            adminUser.setLastPasswordResetTime(DateUtil.now());
            isUpdata = true;
        } else {
            adminUser.setPassword(null);
        }
        try {
            adminUserService.updateById(adminUser);
            if (isUpdata && (Objects.equals(adminUserResult.getIsAdmin(), CommonDefaultValue.DifferentiateStatus.YES))) {
                // 更新主库的t_merchant的超级管理员的密码
                merchantMapper.updateMerchantPwdByParam(adminUserResult.getMerchantId(), adminUserResult.getUsername(), adminUserForm.getPassword(), DateUtil.now());
            }
            AdminUserVo vo1 = new AdminUserVo();
            BeanUtils.copyProperties(adminUserResult, vo1);
            AdminUserVo vo2 = new AdminUserVo();
            BeanUtils.copyProperties(adminUser, vo2);
            //记录日志
            JwtUser user = SecurityUtils.getUser();
            MerchantFieldUtil fieldUtil = new MerchantFieldUtil<AdminUser>();
            MerchantLogFiledVO filedVO = fieldUtil.compareObject(vo1, vo2);
            merchantLogService.saveLog(MerchantLogPageEnum.USER, MerchantLogTypeEnum.USER_MANAGER_UPDATE, filedVO,
                    MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), user.getMerchantCode(), user.getMerchantName(), adminUserResult.getId().toString(), language,IPUtils.getIpAddr(request));
            return Response.returnSuccess("更新成功");
        } catch (Exception e) {
            log.error("更新后台管理用户失败:{}", e.getMessage());
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
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        AdminUser adminUserResult = adminUserService.getById(id);
        if (StringUtils.isEmpty(adminUserResult)) {
            return Response.returnFail("用户不存在");
        }
        if (adminUserResult.getIsAdmin() == 1) {
            return Response.returnFail("超级管理员不能删除");
        }
        boolean isDelete = adminUserService.removeById(id);
        if (isDelete) {
            //记录日志
            JwtUser user = SecurityUtils.getUser();
            merchantLogService.saveLog(MerchantLogPageEnum.USER, MerchantLogTypeEnum.USER_MANAGER_DELETE, null,
                    MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), user.getMerchantCode(), user.getMerchantName(), id.toString(), language,IPUtils.getIpAddr(request));

            return Response.returnSuccess();
        }
        return Response.returnFail("删除失败");
    }


    /**
     * 修改密码
     *
     * @param editPwdForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/editPwd")
    public Response editPwd(HttpServletRequest request, @Validated @RequestBody EditPwdForm editPwdForm, BindingResult bindingResult) {
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        if (editPwdForm.getPassword().length() < 6 || editPwdForm.getPassword().length() > 60) {
            return Response.returnFail("密码长度为6到60位");
        }
        JwtUser user = SecurityUtils.getUser();
        String merchantCode = user.getMerchantCode();
        AdminUser adminUserResult = adminUserService.getOne(new QueryWrapper<AdminUser>().eq("username", editPwdForm.getUsername()).eq("merchant_code",merchantCode));
        if (StringUtils.isEmpty(adminUserResult)) {
            return Response.returnFail("该用户不存在");
        }
        if (!adminUserResult.getPassword().equals(EncryptUtils.encryptPassword(editPwdForm.getOldPassword()))) {
            return Response.returnFail("原密码错误");
        }
        if (!editPwdForm.getPassword().equals(editPwdForm.getSurePassword())) {
            return Response.returnFail("两次密码不一致");
        }
        if (editPwdForm.getOldPassword().equals(editPwdForm.getPassword())) {
            return Response.returnFail("新旧密码一致,请重新输入新密码");
        }

        AdminUser adminUser = new AdminUser();
        adminUser.setId(adminUserResult.getId());
        adminUser.setPassword(EncryptUtils.encryptPassword(editPwdForm.getPassword()));
        adminUser.setPswCode(editPwdForm.getPassword());
        adminUser.setLastPasswordResetTime(DateUtil.now());

        try {
            Boolean update = adminUserService.updateById(adminUser);
            if (update && adminUserResult.getIsAdmin() == 1) {
                String password= AESUtils.aesEncode(editPwdForm.getPassword());
                // 更新主库的t_merchant的超级管理员的密码
                merchantMapper.updateMerchantPwdByParam(adminUserResult.getMerchantId(), adminUserResult.getUsername(), password, DateUtil.now());
                //重置密码1开
                QueryConditionSettingEditReqVO queryConditionVO = new QueryConditionSettingEditReqVO();
                queryConditionVO.setMerchantCode(merchantCode);
                queryConditionVO.setResetPasswordSwitch(1);
                externalMerchantConfigService.editQueryConditionSetting(queryConditionVO);

                //记录日志
                MerchantFieldUtil fieldUtil = new MerchantFieldUtil<AdminUser>();
                MerchantLogFiledVO filedVO = fieldUtil.compareObject(adminUserResult, adminUser);
                merchantLogService.saveLog(MerchantLogPageEnum.USER, MerchantLogTypeEnum.USER_MANAGER_UPDATE, filedVO,
                        MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), user.getMerchantCode(), user.getMerchantName(), adminUserResult.getId().toString(), language,IPUtils.getIpAddr(request));
            }
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("更新用户密码失败:{}", e.getMessage());
            return Response.returnFail("更新失败");
        }
    }

}
