package com.panda.sport.merchant.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Project Name : panda-rcs-order-group
 * @Package Name : panda-rcs-order-group
 * @Description : 用户单关可用额度 入参
 * @Author : Paca
 * @Date : 2021-12-26 21:55
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class UserSingleAvailableLimitReqDto implements Serializable {

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

}
