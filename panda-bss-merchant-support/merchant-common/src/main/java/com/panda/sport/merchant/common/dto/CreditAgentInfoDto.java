package com.panda.sport.merchant.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Project Name : panda-rcs-order-group
 * @Package Name : panda-rcs-order-group
 * @Description : 信用代理信息
 * @Author : Paca
 * @Date : 2021-07-16 23:34
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class CreditAgentInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 信用代理ID，必传
     */
    private String creditId;

    /**
     * 信用代理名称，必传
     */
    private String creditName;

    /**
     * 信用父代理ID，必传，一级代理传0
     */
    private String parentCreditId;
}
