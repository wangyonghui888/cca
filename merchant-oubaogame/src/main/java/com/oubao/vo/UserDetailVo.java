package com.oubao.vo;

import lombok.Data;

@Data
public class UserDetailVo extends BaseVO {

    /**
     * 类型 recordOfOrder,recordOfAccuntChangeHistory
     */
    private String type;

    /**
     * 数据来源(B端或C端)
     */
    private String srcType;

    /**
     * 来源(商户ID)
     */
    private String merchantId;
}