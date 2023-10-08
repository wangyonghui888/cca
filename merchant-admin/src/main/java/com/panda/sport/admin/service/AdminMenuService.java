package com.panda.sport.admin.service;


import com.panda.sport.admin.vo.AdminPermissionVo;


import java.util.List;

/**
 * @auth: YK
 * @Description:菜单栏
 * @Date:2020/5/13 12:41
 */
public interface AdminMenuService {



    List<AdminPermissionVo> getPermissionInMenuIds(List<String> menuIds);

    void allocatePermisson();

    /**
     * 获取是不是信用网
     * @param merchantCode
     * @return
     */
    String getMerchantCredit(String merchantCode);
}
