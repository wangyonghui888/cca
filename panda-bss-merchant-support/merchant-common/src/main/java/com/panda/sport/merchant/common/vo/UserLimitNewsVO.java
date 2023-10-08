package com.panda.sport.merchant.common.vo;

import lombok.Data;


/**
 * @author javier
 * 用户特别额度限制实体
 *  2020-02-03 18:34
 **/
@Data
public class UserLimitNewsVO {
    /**
     * 用户Id
     */
    private String userId;

    /**
     * 最新时间
     */
    private String lastUpdateTime;
}
