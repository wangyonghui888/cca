package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author javier
 * @date 2020/12/15
 * @description 根据代理商查询渠道商户用到的VO
 */
@Data
@ToString
public class MerchantAgentPageVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String merchantName;
    private String merchantId;
    private Integer status;
    private Integer pageNum;
    private Integer pageSize;
    private Integer agentLevel;
}
