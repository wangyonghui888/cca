package com.panda.sport.bc.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.panda.sport.bc.vo.BcAdminMenuVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName JwtUser
 * @auth YK
 * @Description
 * @Date 2020-09-01 11:31
 * @Version  jwt的用户信息
 */
@Getter
@AllArgsConstructor
public class JwtUser implements UserDetails {

    @JsonIgnore
    private final Long id;

    private final String username;

    @JsonIgnore
    private final String password;

    private final String avatar;

    private final String email;

    private final String phone;


    @JsonIgnore
    private final Collection<GrantedAuthority> authorities;

    /**
     * 所属菜单
     */
    @JsonIgnore
    private final List<BcAdminMenuVo> menusBelong;

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
     * @return
     */
    public Collection getRoles() {
        return authorities != null ? authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()) : Collections.emptyList();
    }

    /**
     * 获取菜单
     * @return
     */
    public Collection getMenus() {
        return menusBelong != null ? menusBelong : Collections.emptyList();
    }
}
