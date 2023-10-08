package com.panda.sport.merchant.common.vo;

import com.panda.sport.merchant.common.po.bss.RcsUserSpecialBetLimitConfigPO;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author javier
 * 2021-02-09
 * 风控限额配置
 */
@Data
@ToString
@Accessors
public class RcsUserSpecialBetLimitConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer specialBettingLimitType;

    //单关
    private List<RcsUserSpecialBetLimitConfigPO> rcsUserSpecialBetLimitConfigList1;

    //串关
    private List<RcsUserSpecialBetLimitConfigPO> rcsUserSpecialBetLimitConfigList2;

}