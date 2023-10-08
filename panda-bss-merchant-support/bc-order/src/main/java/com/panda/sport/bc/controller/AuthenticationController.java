package com.panda.sport.bc.controller;

import com.panda.sport.bc.security.AuthorizationUser;
import com.panda.sport.bc.security.JwtUser;
import com.panda.sport.bc.utils.JwtTokenUtil;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AuthenticationController
 * @auth YK
 * @Description 登录控制
 * @Date 2020-09-01 16:07
 * @Version
 */
@Slf4j
@RestController
@RequestMapping("/bc/auth")
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("bcJwtUserDetailsService")
    private UserDetailsService userDetailsService;


    /**
     * @description: 登录验证
     * @Param: [authorizationUser, bindingResult]
     * @return: com.panda.sport.merchant.common.vo.Response
     * @author: YK
     * @date: 2020/9/1 16:09
     */
    @PostMapping("/login")
    public Response login(@Validated @RequestBody AuthorizationUser authorizationUser, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(authorizationUser.getUsername());
            if (jwtUser == null) {
                return Response.returnFail("该用户不存在");
            }

            if (!jwtUser.getPassword().equals(authorizationUser.getPassword())) {
                return Response.returnFail("密码错误");
            }
            if (!jwtUser.isEnabled()) {
                return Response.returnFail("该账号已经冻结");
            }
            // 生成令牌
            Date now = new Date();
            final String token = jwtTokenUtil.generateToken(jwtUser, DateUtils.addHours(now, 4));
            Map<String, Object> map = new HashMap<String, Object>(2) {{
                put("token", token);
                put("user", jwtUser);
            }};
            return Response.returnSuccess(map);
        } catch (Exception e) {
            log.error("验证登录出错:{}", e.getMessage());
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

}
