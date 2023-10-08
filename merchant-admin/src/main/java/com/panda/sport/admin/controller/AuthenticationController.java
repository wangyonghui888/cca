package com.panda.sport.admin.controller;

import com.panda.sport.admin.security.AuthorizationUser;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.impl.AdminMenuServiceImpl;
import com.panda.sport.admin.utils.JwtTokenUtil;
import com.panda.sport.admin.utils.MenuUtils;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.admin.vo.AdminMenuVo;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.admin.service.ExternalMerchantConfigService;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.merchant.MerchantNotice;
import com.panda.sport.merchant.common.utils.EncryptUtils;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.mapper.MerchantNoticeMapper;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auth: YK
 * @Description:登录控制器
 * @Date:2020/5/10 17:16
 */
@Slf4j
@RestController
@RequestMapping("/admin/auth")
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private MerchantNoticeMapper merchantNoticeMapper;

    @Autowired
    private AdminMenuServiceImpl adminMenuService;

    @Autowired
    private ExternalMerchantConfigService externalMerchantConfigService;
    /**
     * 验证登录
     *
     * @param authorizationUser
     * @param bindingResult
     * @return
     */
    @PostMapping("/login")
    public Response login(@Validated @RequestBody AuthorizationUser authorizationUser, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        try {
            final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(authorizationUser.getUsername());
            if (jwtUser == null) {
                return Response.returnFail("该用户不存在");
            }
            log.info("user=" + jwtUser);
            MerchantPO merchantPO = merchantMapper.getMerchantInfo(jwtUser.getMerchantCode());
            log.info("登录用户的merchantPO:" + merchantPO);
            if (StringUtils.isEmpty(merchantPO)) {
                log.error("该商户不存在:" + merchantPO);
                return Response.returnFail("该商户不存在");
            }
            if (merchantPO.getBackendSwitch() != null && merchantPO.getBackendSwitch() == 0) {
                log.error("该商户已经失效:" + merchantPO.getBackendSwitch());
                return Response.returnFail("该商户已经失效");
            }
            if (!jwtUser.getPassword().equals(authorizationUser.getPassword())) {
                log.error("密码错误:" + jwtUser.getPassword() + "," + authorizationUser.getPassword());
                return Response.returnFail("密码错误");
            }
            if (!jwtUser.isEnabled()) {
                return Response.returnFail("该账号已经冻结");
            }
            //验证用户角色权限
            if (jwtUser.getMenus() == null || jwtUser.getMenus().size() < 1) {
                log.error("该账号未配置用户角色权限:" + jwtUser.getMenus());
                return Response.returnFail("该账号未配置用户角色权限");
            } else {
                List<AdminMenuVo> menusList = (List<AdminMenuVo>) jwtUser.getMenus();
                if (!language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
                    for (AdminMenuVo menu : menusList) {
                        menu.setName(menu.getNameEn());
                        if (CollectionUtils.isNotEmpty(menu.getChildren())) {
                            for (AdminMenuVo innerMenu : menu.getChildren()) {
                                innerMenu.setName(innerMenu.getNameEn());
                            }
                        }
                    }
                }
            }

            Long start = System.currentTimeMillis();
            String merchantCreditCode = adminMenuService.getMerchantCredit(jwtUser.getMerchantCode());
            List<MerchantNotice> merchantNoticeList = merchantNoticeMapper.homePopNotice(MenuUtils.getLoginReleaseRange(merchantCreditCode, jwtUser.getAgentLevel()),
                    jwtUser.getMerchantCode() + StringPool.UNDERSCORE);
            Long costTime = System.currentTimeMillis() - start;
            log.info("客户端获取公告ID耗时：{}ms", costTime);

            jwtUser.setOpenVrSport(merchantPO.getOpenVrSport());
            String rest = authorizationUser.getPassword().equals(EncryptUtils.encryptMd5(authorizationUser.getUsername() + merchantPO.getMerchantCode())) ? "1" : "0";
            // 生成令牌
            Date now = new Date();
            final String token = jwtTokenUtil.generateToken(jwtUser, DateUtils.addHours(now, 4));
            Map<String, Object> map = new HashMap<String, Object>(2) {{
                put("token", token);
                put("user", jwtUser);
                put("noticeId", merchantNoticeList.size() > 0 ? merchantNoticeList.get(0).getId() : 0);
                put("rest", rest);
            }};
            //只有超级管理员登录才修改密码
            if (jwtUser.getIsAdmin() != null && jwtUser.getIsAdmin() == 1) {
                Map<String, Object> getQueryMerchantMap = externalMerchantConfigService.getQueryConditionMap(jwtUser.getMerchantCode());
                log.info(jwtUser.getMerchantCode(), "getQueryMerchantMap", getQueryMerchantMap);
                if (getQueryMerchantMap != null && getQueryMerchantMap.containsKey("reset_password_switch") && "false".equals(getQueryMerchantMap.get("reset_password_switch").toString())) {
                    log.error("该商户已经重置密码,需要重新设置密码后再次登录");
                    return Response.returnFail("99990", "该商户已经重置密码,需要重新设置密码后再次登录", map);
                }
            }
            return Response.returnSuccess(map);
        } catch (Exception e) {
            log.error("验证登录出错:{}", e.getMessage());
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/info")
    @PreAuthorize("hasAnyRole('ADMIN_ALL')")
    public Response info() {

        try {
            String userName = SecurityUtils.getUsername();
            return Response.returnSuccess(userName);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR, e.getMessage());
        }
    }

    @PostMapping("/loginOut")
    public Response loginOut(HttpServletRequest request) {

        String authToken = request.getHeader(this.tokenHeader).substring(7);
        String token = jwtTokenUtil.refreshToken(authToken, "quit");
        Date d = jwtTokenUtil.getExpirationDateFromToken(authToken);
        return Response.returnSuccess("退出成功");
    }

}
