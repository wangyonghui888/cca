package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLogHistoryPO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 用户ID
     */
    private Long uid;
    /**
     * 用户名称
     */
    private String username;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 登录次数
     */
    private Integer loginNum;
    /**
     * 订单数量
     */
    private Integer orderNum;
    /**
     * 登录明细说明
     */
    private String logDetail;
    /**
     * 登录类型
     */
    private Integer logType;

    private String merchantCode;

    private String currencyCode;
}