package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.po.merchant.MerchantLevelPO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.AdminPermissionVo;
import com.panda.sport.merchant.common.vo.merchant.CurrencyRateVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantLevelVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : Jeffrey
 * @Date: 2020-01-23 18:37
 * @Description :商户等级表
 */

public interface MerchantLevelService extends BasService {
    /**
     * 商户等级表-查询费率列表
     */
    List<MerchantLevelVO> queryList(MerchantLevelVO merchantRateVO);


    Response detail(String id);
    /**
     * 新增
     *
     * @param merchantRateVO
     * @param language
     * @param ip
     * @return
     */
    Response add(MerchantLevelVO merchantRateVO, Integer userId, String language, String ip);

    /**
     * 修改
     *
     * @param merchantRateVO
     * @param language
     * @return
     */
    boolean update(MerchantLevelVO merchantRateVO, Integer userId, String language,String ip);

    MerchantLevelPO getMerchantLevel(Integer level);

    List<MerchantLevelPO> queryLevelList();

    List<CurrencyRateVO> currencyRateList();

    /**
     * 菜单和权限的所有
     * @param code
     * @return
     */
    Response menuPersionAll(String code);

    // 获取菜单所对应的权限
    List<AdminPermissionVo> getPermissionInMenuIds(List<String> menuList);

    // 添加权限
    Response addRoleMenu(String code, List<String> menuIdList, List<String> PermissionIdList, String agentLevel,HttpServletRequest request);

}
