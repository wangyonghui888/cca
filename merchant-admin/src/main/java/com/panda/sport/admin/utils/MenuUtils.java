package com.panda.sport.admin.utils;

import com.panda.sport.admin.vo.AdminMenuVo;
import com.panda.sport.merchant.common.po.bss.MerchantConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/5/16 21:16
 */
public class MenuUtils {



    /**
     * 获取层级
     *
     * @param
     * @param adminMenuList
     * @return
     */
    public static List<AdminMenuVo> getChild(AdminMenuVo parent, List<AdminMenuVo> adminMenuList) {

        List<AdminMenuVo> childList = new ArrayList<>();
        for (AdminMenuVo adminMenuVo : adminMenuList) {
            if (adminMenuVo.getPid().equals(parent.getId())) {
                adminMenuVo.setParentName(parent.getPath());
                childList.add(adminMenuVo);
            }
        }
        for (AdminMenuVo nav : childList) {
            nav.setChildren(getChild(nav, adminMenuList));
        }
        if (childList.size() == 0) {
            return Collections.emptyList();
        }
        return childList;
    }

    /**
     * 获取发布范围
     *
     * @return
     */
    public static int getReleaseRange(String merchantCreditCode) {

        int releaseRange = 0;
        // 如果是信用网商户
        if (merchantCreditCode != null) {
            releaseRange = 8;
            return releaseRange;
        }
        Integer agentLevel = SecurityUtils.getUser().getAgentLevel();
        switch (agentLevel) {
            case 0:
                releaseRange = 1;
                break;
            case 1:
            case 10:
                releaseRange = 2;
                break;
            case 2:
                releaseRange = 3;
                break;
        }
        return releaseRange;
    }

    /**
     * 首页的登录
     *
     * @param agentLevel
     * @return
     */
    public static int getLoginReleaseRange(String merchantCreditCode,Integer agentLevel) {
        int rg = 0;

        // 如果是信用网商户
        if (merchantCreditCode != null) {
            rg = 8;
            return rg;
        }

        switch (agentLevel) {

            case 2:
                rg = 3;
                break;
            case 1:
                rg = 2;
                break;
            case 0:
                rg = 1;
                break;
        }
        return rg;
    }


    /**
     * 时间转换
     *
     * @param sendTime
     * @return
     */


}
