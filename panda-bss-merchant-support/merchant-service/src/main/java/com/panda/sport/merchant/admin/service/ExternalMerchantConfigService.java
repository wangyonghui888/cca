package com.panda.sport.merchant.admin.service;

import com.panda.sport.merchant.common.vo.merchant.QueryConditionSettingEditReqVO;
import com.panda.sport.merchant.common.vo.merchant.QueryConditionSettingQueryRespVO;

import java.util.Map;

/**
 * @author :  ives
 * @Description :  对外商户配置服务类
 * @Date: 2022-01-24
 */
public interface ExternalMerchantConfigService {

    /**
     * 获取查询条件设置
     * @param merchantCode
     * @return QueryConditionSettingVO
     */
    QueryConditionSettingQueryRespVO getQueryConditionSetting(String merchantCode);

    /**
     * 修改查询条件设置
     * @param editReqVO
     * @return boolean
     */
    boolean editQueryConditionSetting(QueryConditionSettingEditReqVO editReqVO);

    Map<String,Object> getQueryConditionMap(String merchantCode);
}
