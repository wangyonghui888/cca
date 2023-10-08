package com.panda.sport.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.admin.service.AdminRolesMenusService;
import com.panda.sport.admin.utils.MenuUtils;
import com.panda.sport.admin.vo.AdminMenuVo;
import com.panda.sport.merchant.common.po.merchant.AdminMenu;
import com.panda.sport.merchant.common.po.merchant.AdminRolesMenus;
import com.panda.sport.merchant.common.po.merchant.AdminUser;
import com.panda.sport.merchant.mapper.AdminRolesMenusMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @auth: YK
 * @Description:用户所属权限
 * @Date:2020/5/13 13:49
 */
@Service
public class AdminRolesMenusServiceImpl extends ServiceImpl<AdminRolesMenusMapper, AdminRolesMenus> implements AdminRolesMenusService {

    @Autowired
    private AdminMenuServiceImpl adminMenuService;


    @Override
    public List<AdminMenuVo> mapToGrantedMenu(AdminUser adminUser) {

        QueryWrapper<AdminRolesMenus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", adminUser.getRoleId());
        List<AdminRolesMenus> adminRolesMenusList = list(queryWrapper);
        Set<Long> menuId = adminRolesMenusList.stream().map(AdminRolesMenus::getMenuId).collect(Collectors.toSet());
        if (menuId.size() > 0) {
            QueryWrapper<AdminMenu> adminMenuQueryWrapper = new QueryWrapper<>();
            adminMenuQueryWrapper.in("id", menuId);
            List<AdminMenu> adminMenuList = adminMenuService.list(adminMenuQueryWrapper);

            List<AdminMenuVo> adminMenuVoList = new ArrayList<>();
            List<AdminMenuVo> rootMenu = new ArrayList<>();

            for (AdminMenu adminMenu : adminMenuList) {
                AdminMenuVo adminMenuVo = new AdminMenuVo();
                BeanUtils.copyProperties(adminMenu, adminMenuVo);
                adminMenuVoList.add(adminMenuVo);
            }

            for (AdminMenuVo adminMenuVo : adminMenuVoList) {
                if (adminMenuVo.getPid() == 0) {
                    adminMenuVo.setParentName("");
                    rootMenu.add(adminMenuVo);
                }
            }

            for (AdminMenuVo amv : rootMenu) {
                List<AdminMenuVo> childList = MenuUtils.getChild(amv, adminMenuVoList);
                amv.setChildren(childList);
            }

            return rootMenu.size() > 0 ? rootMenu : null;
        }
        return null;
    }
}
