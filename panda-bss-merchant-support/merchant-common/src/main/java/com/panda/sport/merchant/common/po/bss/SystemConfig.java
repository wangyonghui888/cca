package com.panda.sport.merchant.common.po.bss;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.panda.sport.merchant.common.constant.SystemConfigConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class SystemConfig {

    private Long id;
    private String configKey;
    private String configValue;
    private Long createTime;
    private String createBy;
    private Long updateTime;
    private String updateBy;
    private String remark;

    public static FrontendMerchantGroupDomainPO toPO(SystemConfig systemConfig){
        FrontendMerchantGroupDomainPO po = new FrontendMerchantGroupDomainPO();
        try {
            String configKey = systemConfig.getConfigKey();
            configKey = systemConfig.getConfigKey().replace(FrontendMerchantGroupDomainPO.merchantKey, "");
            po.setGroupId(systemConfig.getId());
            po.setGroupName(configKey);
            po.setStatus(Integer.parseInt(systemConfig.getConfigValue()));
            if (StringUtils.isBlank(systemConfig.getRemark())) {
                return po;
            }
            JSONObject remark = JSON.parseObject(systemConfig.getRemark(), JSONObject.class);
            po.setPc(remark.getString("pc"));
            po.setH5(remark.getString("h5"));
            String merchantCodeListStr = remark.getString("merchantCodeList");
            if (StringUtils.isNotBlank(merchantCodeListStr)) {
                po.setMerchantCodeSet(Arrays.stream(merchantCodeListStr.split(",")).collect(Collectors.toSet()));
            }
            return po;
        }catch (Exception e){
         log.error("SystemConfig toPO ERROR ,reason for KEY :{}",systemConfig.getConfigKey(),e);
            return null;
        }
    }

    public static VideoMerchantGroupDomainPO toVideoPO(SystemConfig systemConfig){
        VideoMerchantGroupDomainPO po = new VideoMerchantGroupDomainPO();
        try {
            String configKey = systemConfig.getConfigKey();
            configKey = systemConfig.getConfigKey().replace(SystemConfigConstant.VIDEO_MERCHANT_GROUP_PREFIX, "");
            po.setGroupId(systemConfig.getId());
            po.setGroupName(configKey);
            po.setStatus(Integer.parseInt(systemConfig.getConfigValue()));
            if (StringUtils.isBlank(systemConfig.getRemark())) {
                return po;
            }
            JSONObject remark = JSON.parseObject(systemConfig.getRemark(), JSONObject.class);
            po.setVideoAll(remark.getString(SystemConfigConstant.VIDEO_COLUMN_VIDEO_ALL));
            po.setVideoExcitingEditing(remark.getString(SystemConfigConstant.VIDEO_COLUMN_VIDEO_EXCITING_EDITING));
            String merchantCodeListStr = remark.getString("merchantCodeList");
            if (StringUtils.isNotBlank(merchantCodeListStr)) {
                po.setMerchantCodeSet(Arrays.stream(merchantCodeListStr.split(",")).collect(Collectors.toSet()));
            }
            return po;
        }catch (Exception e){
            log.error("SystemConfig toPO ERROR ,reason for KEY :{}",systemConfig.getConfigKey(),e);
            return null;
        }
    }

    public static List<FrontendMerchantGroupDomainPO> toPO(List<SystemConfig> systemConfigList){
        if(CollectionUtils.isEmpty(systemConfigList)){
            return null;
        }
        List<FrontendMerchantGroupDomainPO> result = Lists.newArrayList();
        for (SystemConfig systemConfig : systemConfigList) {
            FrontendMerchantGroupDomainPO po = toPO(systemConfig);
            if(po != null){
                po.setAlarmNum(100);
                po.setTimes(100);
                po.setTimeType(4);
                result.add(po);
            }
        }
        return result;
    }

    public static List<VideoMerchantGroupDomainPO> toVideoPO(List<SystemConfig> systemConfigList){
        if(CollectionUtils.isEmpty(systemConfigList)){
            return null;
        }
        List<VideoMerchantGroupDomainPO> result = Lists.newArrayList();
        for (SystemConfig systemConfig : systemConfigList) {
            VideoMerchantGroupDomainPO po = toVideoPO(systemConfig);
            if(po != null){
                po.setAlarmNum(100);
                po.setTimes(100);
                po.setTimeType(4);
                result.add(po);
            }
        }
        return result;
    }

    public static FrontendMerchantGroupDomainPO getDefaultConfig(List<SystemConfig> systemConfigList){
        if(CollectionUtils.isEmpty(systemConfigList)){
            return null;
        }
        for (SystemConfig systemConfig : systemConfigList) {
            if(systemConfig.getConfigKey().equals(SystemConfigConstant.FRONT_MERCHANT_GROUP_PREFIX + "系统预设商户组")
            || systemConfig.getId().equals( 10L )){
                FrontendMerchantGroupDomainPO po = toPO(systemConfig);
                if(po != null){
                    po.setAlarmNum(100);
                    po.setTimes(100);
                    po.setTimeType(4);
                    return po;
                }
            }
        }
        return null;
    }

    public static VideoMerchantGroupDomainPO getVideoDefaultConfig(List<SystemConfig> systemConfigList){
        if(CollectionUtils.isEmpty(systemConfigList)){
            return null;
        }
        for (SystemConfig systemConfig : systemConfigList) {
            if(systemConfig.getConfigKey().equals(SystemConfigConstant.VIDEO_MERCHANT_GROUP_PREFIX + "系统预设商户组")
                    || systemConfig.getId().equals( 10L )){
                VideoMerchantGroupDomainPO po = toVideoPO(systemConfig);
                if(po != null){
                    po.setAlarmNum(100);
                    po.setTimes(100);
                    po.setTimeType(4);
                    return po;
                }
            }
        }
        return null;
    }

}
