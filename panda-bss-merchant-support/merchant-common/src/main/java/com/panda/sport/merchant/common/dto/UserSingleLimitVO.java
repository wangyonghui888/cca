package com.panda.sport.merchant.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class UserSingleLimitVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户ID
     */
    private String merchantId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 赛事ID
     */
    private String matchId;

    /**
     * 玩法ID
     */
    private Long playId;

}
