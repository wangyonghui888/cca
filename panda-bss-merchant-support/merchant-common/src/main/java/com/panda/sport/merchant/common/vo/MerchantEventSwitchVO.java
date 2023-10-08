package com.panda.sport.merchant.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantEventSwitchVO {

    /**
     * goalEvent   进球事件(0关,1开)
     */
    //private Integer goalEvent;
    /**
     * cornerEvent  角球事件(0关,1开)
     */
    private Integer cornerEvent;
    /**
     * penaltyEvent 罚牌事件(0关,1开)
     */
    private Integer penaltyEvent;
    /**
     * videoTraffic   视频流量消耗
     */
    //private Integer videoTraffic;

    /**
     * 商户code
     */
    //private String merchantCode;

    /**
     * 精彩回放开关
     */
    private Integer eventSwitch;

}
