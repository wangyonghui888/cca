package com.panda.sport.bc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.bc.service.BcAdminRolesMenusService;
import com.panda.sport.bc.utils.MenuUtils;
import com.panda.sport.bc.vo.BcAdminMenuVo;
import com.panda.sport.bc.mapper.BcAdminRolesMenusMapper;
import com.panda.sport.merchant.common.po.bcorder.BcAdminMenu;
import com.panda.sport.merchant.common.po.bcorder.BcAdminRolesMenus;
import com.panda.sport.merchant.common.po.bcorder.BcAdminUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @ClassName AdminRolesMenusServiceImpl
 * @auth YK
 * @Description
 * @Date 2020-09-01 14:37
 * @Version
 */
@Service
public class BcAdminRolesMenusServiceImpl extends ServiceImpl<BcAdminRolesMenusMapper, BcAdminRolesMenus> implements BcAdminRolesMenusService {

    @Autowired
    private BcAdminMenuServiceImpl adminMenuService;

    /**
    * @description: 获取菜单
    * @Param: [adminUser]
    * @return: java.util.List<com.panda.sport.bc.vo.BcAdminMenuVo>
    * @author: YK
    * @date: 2020/9/11 12:06
    */
    @Override
    public List<BcAdminMenuVo> mapToGrantedMenu(BcAdminUser adminUser) {
        QueryWrapper<BcAdminRolesMenus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", adminUser.getRoleId());
        List<BcAdminRolesMenus> adminRolesMenusList = list(queryWrapper);
        Set<Long> menuId = adminRolesMenusList.stream().map(e -> e.getMenuId()).collect(Collectors.toSet());
        if (menuId.size() > 0) {
            QueryWrapper<BcAdminMenu> adminMenuQueryWrapper = new QueryWrapper<>();
            adminMenuQueryWrapper.in("id", menuId);
            List<BcAdminMenu> adminMenuList = adminMenuService.list(adminMenuQueryWrapper);

            List<BcAdminMenuVo> bcAdminMenuVoList = new ArrayList<>();
            List<BcAdminMenuVo> rootMenu = new ArrayList<>();

            for (BcAdminMenu adminMenu : adminMenuList) {
                BcAdminMenuVo bcAdminMenuVo = new BcAdminMenuVo();
                BeanUtils.copyProperties(adminMenu, bcAdminMenuVo);
                bcAdminMenuVoList.add(bcAdminMenuVo);
            }

            for (BcAdminMenuVo bcAdminMenuVo : bcAdminMenuVoList) {
                if (bcAdminMenuVo.getPid() == 0) {
                    bcAdminMenuVo.setParentName("");
                    rootMenu.add(bcAdminMenuVo);
                }
            }

            for (BcAdminMenuVo amv : rootMenu) {
                List<BcAdminMenuVo> childList = MenuUtils.getChild(amv, bcAdminMenuVoList);
                amv.setChildren(childList);
            }

            return rootMenu.size() > 0 ? rootMenu : null;
        }
        return null;
    }


}
