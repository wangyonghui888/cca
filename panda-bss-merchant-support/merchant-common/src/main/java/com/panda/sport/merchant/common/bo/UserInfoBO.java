package com.panda.sport.merchant.common.bo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author amos
 */
@Data
@ToString
public class UserInfoBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long uid;

    private String userName;

    private String merchantCode;

    private String merchantName;

    private String currencyCode;

    private Long createTime;

    private Long loginTime;

    private Double maxBetDouble;

    private String createTimeStr;

    private String lastLoginTimeStr;

    private Integer logType;
    //日志详情
    private String logDetail;
}
