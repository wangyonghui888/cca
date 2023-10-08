//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.panda.sport.merchant.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Project Name : panda-rcs-order-group
 * @Package Name : panda-rcs-order-group
 * @Description : 信用模式配置
 * @Author : Paca
 * @Date : 2021-04-27 15:32
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class CreditConfigApiDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * merchantId = null && creditId = null    查询模板配置
     * merchantId != null && creditId = null   查询商户配置
     * merchantId != null && creditId != null  查询信用代理配置
     */
    /**
     * 商户ID，为空查询模板配置
     */
    private String merchantCode;

    private Long timestamp;

    private String signature;

    /**
     * 信用代理信息，支持多个代理设置同一套配置
     */
    private List<CreditAgentInfoDto> creditAgentInfoList;

    /**
     * 玩法最高投注限额配置
     */
    private List<CreditSinglePlayConfigDto> singlePlayConfigList;

    /**
     * 单场最高投注限额配置
     */
    private List<CreditSingleMatchConfigDto> singleMatchConfigList;

    /**
     * 串关每日投注限额配置
     */
    private List<CreditSeriesConfigDto> seriesConfigList;
}
