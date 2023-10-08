package com.panda.sport.merchant.common.vo;

import lombok.Data;

/**
 * @author javier
 * @date 2021/2/4
 * 特殊单注单场限额  和   特殊VIP限额
 */
@Data
public class UserLimitSpecialDetailVO {
    /**
     * 特殊百分比限额
     */
    private Float userLimitPercent;

    private Boolean isAll;

    private Long footballBetLimit;

    private Long footballMatchLimit;

    private Long basketballBetLimit;

    private Long basketballMatchLimit;

    private Long otherBetLimit;

    private Long otherMatchLimit;

    private Long manyBetLimit;

    private Long manyMatchLimit;

    private String remark;

}
