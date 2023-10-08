package com.panda.sport.merchant.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.merchant.common.constant.DataSourceConstants;
import com.panda.sport.merchant.common.constant.MerchantDefaultValue;
import com.panda.sport.merchant.common.vo.merchant.QueryConditionSettingEditReqVO;
import com.panda.sport.merchant.mapper.ExternalMerchantConfigMapper;
import com.panda.sport.merchant.admin.service.ExternalMerchantConfigService;
import com.panda.sport.merchant.common.po.merchant.MerchantConfig;
import com.panda.sport.merchant.common.vo.merchant.QueryConditionSettingQueryRespVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author :  ives
 * @Description :  对外商户配置服务类
 * @Date: 2022-01-24
 */
@Service
public class ExternalMerchantConfigServiceImpl extends ServiceImpl<ExternalMerchantConfigMapper, MerchantConfig> implements ExternalMerchantConfigService {


    /**
     * 获取查询条件设置
     * @param merchantCode
     * @return QueryConditionSettingVO
     */
    @Override
    public QueryConditionSettingQueryRespVO getQueryConditionSetting(String merchantCode) {
        QueryConditionSettingQueryRespVO queryConditionSettingVO = new QueryConditionSettingQueryRespVO();

        QueryWrapper<MerchantConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DataSourceConstants.MERCHANT_CODE,merchantCode);
        MerchantConfig merchantConfig = getOne(queryWrapper);

        if(null == merchantConfig){
            queryConditionSettingVO.setDefaultTimeType(MerchantDefaultValue.MerchantQueryConditionSetting.defaultTimeType);
            queryConditionSettingVO.setIsNatureDay(MerchantDefaultValue.MerchantQueryConditionSetting.isNatureDay);
            return  queryConditionSettingVO;
        }

        BeanUtils.copyProperties(merchantConfig,queryConditionSettingVO);
        return  queryConditionSettingVO;
    }

    /**
     * 修改查询条件设置
     * @param editReqVO
     * @return boolean
     */
    @Override
    public boolean editQueryConditionSetting(QueryConditionSettingEditReqVO editReqVO) {
        QueryWrapper<MerchantConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DataSourceConstants.MERCHANT_CODE,editReqVO.getMerchantCode());
        MerchantConfig merchantConfig = getOne(queryWrapper);

        if (null == merchantConfig){
            merchantConfig = new MerchantConfig();
            merchantConfig.setMerchantCode(editReqVO.getMerchantCode());
            merchantConfig.setDefaultTimeType(editReqVO.getDefaultTimeType());
            merchantConfig.setIsNatureDay(editReqVO.getIsNatureDay());
            merchantConfig.setResetPasswordSwitch(editReqVO.getResetPasswordSwitch());
            merchantConfig.setAbnormalClickTime(editReqVO.getAbnormalClickTime());
            return save(merchantConfig);
        }
        Long tempId = merchantConfig.getId();
        BeanUtils.copyProperties(editReqVO,merchantConfig);
        merchantConfig.setId(tempId);

        return updateById(merchantConfig);
    }

    @Override
    public Map<String, Object> getQueryConditionMap(String merchantCode) {
        QueryWrapper<MerchantConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DataSourceConstants.MERCHANT_CODE,merchantCode);
        return getMap(queryWrapper);
    }
}
