package com.panda.sport.merchant.common.vo;

import com.panda.sport.merchant.common.po.bss.RcsUserSpecialBetLimitConfigPO;
import lombok.Data;

import java.util.List;

/**
 * @author javier
 * @date 2021/2/9
 * TODO 风控限额详情
 */
@Data
public class RcsUserConfigDetailVO {
    private String userId;
    private Integer specialBettingLimitType;
    private Long specialBettingLimitTime;
    private String specialBettingLimitRemark;
    private Integer specialBettingLimitDelayTime;
    private Integer marketLevel;
    private List<RcsUserSpecialBetLimitConfigPO> rcsUserSpecialBetLimitConfigList;
}
