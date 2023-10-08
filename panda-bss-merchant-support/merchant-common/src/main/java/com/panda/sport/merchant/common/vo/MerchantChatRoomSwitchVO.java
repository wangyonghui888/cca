package com.panda.sport.merchant.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantChatRoomSwitchVO {

    /**
     * 聊天室开关(0关,1开)
     */
    private Integer chatRoomSwitch;

    /**
     * 发言最低累计投注额(0关,1开)
     */
    private Integer chatMinBetAmount;

    /**
     * 是否默认设置(0默认,1自定义)
     */
    private Integer isDefault;

    /**
     * 前三天累计投注金额
     */
    private Double threeDayAmount;

    /**
     * 前七天累计投注金额
     */
    private Double sevenDayAmount;

    /**
     * 代理级别(0:直营站点,1:渠道站点,2:渠道站点,10:代理商)
     */
    private String agentLevel;

    private String merchantCode;
    private String merchantName;
    private String merchantId;

}
