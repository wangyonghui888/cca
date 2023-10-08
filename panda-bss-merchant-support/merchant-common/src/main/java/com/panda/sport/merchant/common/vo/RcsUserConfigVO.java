package com.panda.sport.merchant.common.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author javier
 * @date 2021/2/9
 *  TODO 用户限额接收参数信息
 */
@Data
public class RcsUserConfigVO {
    private String userId;
    private Integer specialBettingLimit;
    private Integer betExtraDelay;
    private Integer marketLevel;
    private String remarks;
    private Integer percentage;
    private List<Long> sportIdList;
}
