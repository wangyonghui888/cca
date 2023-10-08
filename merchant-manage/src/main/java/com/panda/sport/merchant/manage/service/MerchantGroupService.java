package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.vo.MerchantGroupVO;
import com.panda.sport.merchant.common.po.bss.MerchantGroupPO;
import com.panda.sport.merchant.common.vo.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 * @date 2021/8/20
 * @TIME 11:24
 */
public interface MerchantGroupService {

    Response createMerchantGroup(MerchantGroupVO merchantGroupPO);

    Response updateMerchantGroup(MerchantGroupVO merchantGroupPO);

    Response updateMerchantGroupNew(HttpServletRequest request,MerchantGroupVO merchantGroupPO);

    Response deleteMerchantGroup(MerchantGroupVO merchantGroupPO);

    List<MerchantGroupVO> selectMerchantGroup(MerchantGroupVO merchantGroupPO);

    void autoChangeDomain();

    void autoChangeDomainThird();

    void changDomainEnable();

    void changDomainEnableThird();

    void updateGroupNum(HttpServletRequest request,MerchantGroupPO merchantGroupPO);

    Integer getThirdEnable();

    void updateThirdEnable(String enable);

    // 获取
    Response selectGroupDomain();
}
