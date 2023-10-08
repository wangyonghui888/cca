package com.panda.sport.merchant.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Project Name : panda-rcs-order-group
 * @Package Name : panda-rcs-order-group
 * @Description : 用户单关可用额度 出参
 * @Author : Paca
 * @Date : 2021-12-26 21:55
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class UserSingleAvailableLimitResDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户ID
     */
    private String merchantId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 赛事ID
     */
    private Long matchId;

    /**
     * 玩法ID
     */
    private Long playId;

    /**
     * 1-早盘，0-滚球
     */
    private Integer matchType;

    /**
     * 操盘平台，MTS、PA
     */
    private String riskManagerCode;

    /**
     * 赛种
     */
    private Long sportId;

    /**
     * 用户特殊限额类型，0-无，1-标签限额，2-特殊百分比限额，3-特殊单注单场限额，4-特殊VIP限额
     */
    private String userSpecialLimitType;

    /**
     * 玩法限额（玩法初始限额）
     */
    private BigDecimal playLimit;

    /**
     * 玩法可用限额
     */
    private BigDecimal playAvailableLimit;

}
