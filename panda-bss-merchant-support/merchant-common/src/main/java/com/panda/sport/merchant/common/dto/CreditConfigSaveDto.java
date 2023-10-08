package com.panda.sport.merchant.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Project Name : panda-rcs-order-group
 * @Package Name : panda-rcs-order-group
 * @Description : 信用代理配置
 * @Author : Paca
 * @Date : 2021-07-16 23:32
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class CreditConfigSaveDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户ID，必传
     */
    private Long merchantId;

    /**
     * 信用代理信息，支持多个代理设置同一套配置
     */
    private List<CreditAgentInfoDto> creditAgentInfoList;

    /**
     * 玩法限额
     */
    private List<CreditSinglePlayConfigDto> singlePlayConfigList;

    /**
     * 单场限额，只有代理维度才有单场限额，用户维度没有
     */
    private List<CreditSingleMatchConfigDto> singleMatchConfigList;

    /**
     * 串关限额
     */
    private List<CreditSeriesConfigDto> seriesConfigList;
}
