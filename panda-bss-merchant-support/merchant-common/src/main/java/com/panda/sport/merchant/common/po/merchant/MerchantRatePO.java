package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class MerchantRatePO extends BasePO{

    private Long id;

    private String computingStandard;

    private Long rangeAmountBegin;

    private Long rangeAmountEnd;

    private Double terraceRate;

    private Integer paymentCycle;

/*
    private Long vipAmount;

    private Integer vipPaymentCycle;

    private Long techniqueAmount;

    private Integer techniquePaymentCycle;
*/

    private String remarks;

    private Long createTime;

    private String createUser;

    private Long modifyTime;

    private String modifyUser;

    private static final long serialVersionUID = 1L;

}
