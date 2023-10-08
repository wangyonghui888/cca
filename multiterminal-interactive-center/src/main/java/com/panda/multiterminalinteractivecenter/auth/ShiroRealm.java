package com.panda.multiterminalinteractivecenter.auth;

import com.panda.multiterminalinteractivecenter.entity.Permissions;
import com.panda.multiterminalinteractivecenter.entity.User;
import com.panda.multiterminalinteractivecenter.service.impl.LoginServiceImpl;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.vo.LoginUserVo;
import com.panda.multiterminalinteractivecenter.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.auth
 * @Description :  TODO
 * @Date: 2022-03-13 13:47:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private LoginServiceImpl loginService;

    /**
     * 重写，绕过身份令牌异常导致的shiro报错
     * @param authenticationToken
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken authenticationToken){
        return authenticationToken instanceof AuthTokenVo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取登录用户名
        User u = (User)principalCollection.getPrimaryPrincipal();
        // 查询用户名称
        LoginUserVo user = loginService.creatLoginUserVoByName(u.getName());
        // 添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (RoleVo role : user.getRoles()) {
            // 添加角色
            simpleAuthorizationInfo.addRole(role.getRoleName());
            for (Permissions permission : role.getPermissions()) {
                // 添加权限
                simpleAuthorizationInfo.addStringPermission(permission.getUrl());
            }
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        //获得token
        String token = (String)authenticationToken.getCredentials();
        //获得token中的用户信息
        String username = JWTUtil.getUsername(token);
        //判空
        if(StringUtils.isBlank(username)){
            throw new AuthenticationException(AuthConstant.TOKEN_BLANK);
        }

        User user = loginService.findUserByName(username);
        if (user == null) {
            // 这里返回后会报出对应异常
            throw new AuthenticationException(AuthConstant.TOKEN_INVALID);
        }else if(!(JWTUtil.verify(token, username, user.getPassword()))){
            throw new AuthenticationException(AuthConstant.TOKEN_EXPIRE);
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user,
                token,
                getName());
        return authenticationInfo;
    }
}
