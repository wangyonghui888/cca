package com.panda.sport.bc.utils;

import com.panda.sport.bc.vo.BcAdminMenuVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName MenuUtils
 * @auth YK
 * @Description 菜单栏
 * @Date 2020-09-01 15:27
 * @Version
 */
public class MenuUtils {

    /**
    * @description:   获取层级
    * @Param: [parent, adminMenuList]
    * @return: java.util.List<com.panda.sport.bc.vo.BcAdminMenuVo>
    * @author: YK
    * @date: 2020/9/11 12:08
    */
    public static List<BcAdminMenuVo> getChild(BcAdminMenuVo parent, List<BcAdminMenuVo> adminMenuList) {

        List<BcAdminMenuVo> childList = new ArrayList<>();
        for (BcAdminMenuVo bcAdminMenuVo : adminMenuList) {
            if (bcAdminMenuVo.getPid().equals(parent.getId())) {
                bcAdminMenuVo.setParentName(parent.getPath());
                childList.add(bcAdminMenuVo);
            }
        }
        for (BcAdminMenuVo nav : childList) {
            nav.setChildren(getChild(nav,adminMenuList));
        }
        if (childList.size()==0) {
            return Collections.emptyList();
        }
        return childList;
    }

}
