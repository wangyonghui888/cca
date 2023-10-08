package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MerchantLevelPO extends BasePO {
    private Long id;

    private Integer level;

    private String levelName;

    private String rateId;

    private String logo;

    private Long rangeAmountBegin;

    private Long rangeAmountEnd;

    private Double terraceRate;

    private Integer paymentCycle;

    private Long vipAmount;

    private Integer vipPaymentCycle;

    private Long techniqueAmount;

    private Integer techniquePaymentCycle;

    private String remarks;

    private String computingStandard;

}