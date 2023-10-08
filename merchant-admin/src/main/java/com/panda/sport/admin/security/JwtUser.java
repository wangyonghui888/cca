package com.panda.sport.admin.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.panda.sport.admin.vo.AdminMenuVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auth: YK
 * @Description:jwtuser的实体类
 * @Date:2020/5/10 17:07
 */
@Data
@AllArgsConstructor
public class JwtUser implements UserDetails {

    @JsonIgnore
    private final String id;

    private final String username;

    @JsonIgnore
    private final String password;

    private final String avatar;

    private final String email;

    private final String phone;

    /**
     * 所属商户ID
     */
    private final String merchantId;

    /**
     * 所属商户的code
     */
    private final String merchantCode;

    /**
     * 所属商户的名称
     */
    private final String merchantName;
    private Integer openVrSport;

    /**
     * 所属商户的父级商户名称
     */
    @JsonIgnore
    private final String parentMerchantCode;

    /**
     * 商户等级
     */
    private final Integer agentLevel;

    /**
     * 是否超级管理员  0 否 1是
     */
    private final Integer isAdmin;


    @JsonIgnore
    private final Collection<GrantedAuthority> authorities;

    /**
     * 所属菜单
     */
    @JsonIgnore
    private final List<AdminMenuVo> menusBelong;

    @JsonIgnore
    private final boolean enabled;

    @JsonIgnore
    private String createTime;

    @JsonIgnore
    private final String lastPasswordResetDate;

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 获取权限
     *
     * @return
     */
    public Collection getRoles() {
        return authorities != null ? authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()) : Collections.emptyList();
    }

    /**
     * 获取菜单
     *
     * @return
     */
    public Collection getMenus() {
        return menusBelong != null ? menusBelong : Collections.emptyList();
    }
}
