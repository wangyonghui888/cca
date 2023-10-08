package com.panda.sport.merchant.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantVideoManageVo {

    /**
     * 长时间未操作暂停视频(0关,1开)
     */
    private Integer closedWithoutOperation;

    /**
     * 视频设置(0默认,1自定义)
     */
    private Integer videoSettings;

    /**
     * 默认视频观看时长设置(15分钟)
     */
    private Integer viewingTime;

    /**
     * 自定义视频观看时长设置(5~120分钟)
     */
    private Integer customViewTime;

    /**
     * 不可背景播放(0关,1开)
     */
    private Integer noBackgroundPlay;

    /**
     * 商户编码
     */
    private String merchantCode;

    private Integer pageSize;
    private Integer pageIndex;
    private String sort;
    private String orderBy;
}
