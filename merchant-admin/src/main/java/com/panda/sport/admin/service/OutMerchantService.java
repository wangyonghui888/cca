package com.panda.sport.admin.service;


import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.po.bss.TMerchantKey;
import com.panda.sport.merchant.common.vo.MerchantOrderOperationVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.AdminUserNameUpdateReqVO;
import com.panda.sport.merchant.common.vo.merchant.CurrencyRateVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantOrderVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface OutMerchantService {

    Response selectList(HttpServletRequest request, String merchantName, Integer status,Integer merchantTag, Integer pageIndex, Integer pageSize);

    Response getMerchantDetail(String id);

    Response updateMerchant(MerchantVO merchantVO, String language,String ip);

    Response createMerchant(MerchantVO merchantVO, String language,String ip);

    Response<PageInfo<TMerchantKey>> queryKeyList(String merchantName, String parentId, Integer pageSize, Integer pageIndex, String language);

    Response<Object> generateKey();

    Response<Object> updateKey(String key, String startTime, String endTime, String language, String ip, String keyLabel);

    Response<Object> updateMerchantStatus(String merchantCode, String status, String language ,String ip);

    Response<Object> updateMerchantBackendStatus(String merchantCode, String status, String language, String ip);

    Response<TMerchantKey> getKey(String language);

    Map exportMerchantReport(HttpServletResponse response, MerchantOrderVO requestVO, String token, String language);

    Response queryMerchantReportList(MerchantOrderVO merchantOrderRequestVO);

    /**
     * 根据商户将时间段内的数据合并为一条
     */
    Response listGroupByMerchant(MerchantOrderVO merchantOrderRequestVO);

    Response queryMerchantTop10(String orderBy);

    Response queryCurrentMonthMerchantList();

    Response createAdmin(String id, String adminName, String adminPassword, String language,String ip);

    Response<?> updateAdminPassword(String id, String adminNewPassword, String language,String ip);

    Response amountGrowthRateTop10();

    Response queryMerchantListByParam();

    List<String> queryMerchantList();

    Response merchantChannelOrderTop10();

    Response<Object> deleteSubAgent(String merchantCode, String parentId);

    Response getMerchantLanguageList();

    Response getMerchantListTree();
    Response queryMechantAgentList(String merchantCode);

    Response orderOperation(MerchantOrderOperationVO merchantOrderOperationVO ,String ip);

    List<CurrencyRateVO> currencyRateList();

    /**
     * 对外商户后台-账户中心-二级商户管理-修改超级管理员名称
     * @param adminUserNameUpdateReqVO
     * @param language
     * @return Response
     */
    Response updateAdminUserName(AdminUserNameUpdateReqVO adminUserNameUpdateReqVO, String language,String ipAddress);

}
