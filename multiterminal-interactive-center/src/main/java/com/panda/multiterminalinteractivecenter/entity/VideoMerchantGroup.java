package com.panda.multiterminalinteractivecenter.entity;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.sport.merchant.common.constant.SystemConfigConstant;
import com.panda.sport.merchant.common.po.bss.SystemConfig;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class VideoMerchantGroup {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long groupId;

    /**
     * 分组名称
     */
    private String groupName;


    /**
     * 分组类型
     */
    private String groupType;

    /**
     * 是否启用(1为启用,2为禁用)
     */
    private Integer status;

    /**
     * 时间类型(1为分钟,2为小时,3为日,4为月)
     */
    private Integer timeType;

    /**
     * 时间值
     */
    private Integer times;

    /**
     * 报警阈值
     */
    private Integer alarmNum;

    /**
     * 全量域名
     */
    private String videoAll;
    /**
     * 精彩剪辑
     */
    private String videoExcitingEditing;

    /**
     * 商户编码集合
     */
    private List<String> merchantCodeList;
    private List<String> oldMerchantCodeList;

    //添加请求参数 "type" 0 选择商户 1 切换方案 2 启用/禁用 3切换频率
    private Integer type;

    private Long createTime;

    private String createUser;

    private Long updateTime;

    private String updateUser;

    public SystemConfig toSystemConfig(){
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setConfigKey(SystemConfigConstant.VIDEO_MERCHANT_GROUP_PREFIX + this.groupName);
        systemConfig.setConfigValue(this.status.toString());
        systemConfig.setCreateTime(this.createTime);
        systemConfig.setCreateBy(this.createUser);
        systemConfig.setUpdateTime(this.updateTime);
        systemConfig.setUpdateBy(this.updateUser);

        JSONObject remark = new JSONObject();
        if(CollectionUtils.isNotEmpty(this.merchantCodeList)){
            remark.put("merchantCodeList", String.join(",", this.merchantCodeList));
        }
        if(StringUtils.isNotEmpty(this.videoAll)){
            remark.put(SystemConfigConstant.VIDEO_COLUMN_VIDEO_ALL, this.videoAll);
        }
        if(StringUtils.isNotEmpty(this.videoExcitingEditing)){
            remark.put(SystemConfigConstant.VIDEO_COLUMN_VIDEO_EXCITING_EDITING, this.videoExcitingEditing);
        }
        if(remark.size() > 0){
            systemConfig.setRemark(remark.toJSONString());
        }
        return systemConfig;
    }

}
