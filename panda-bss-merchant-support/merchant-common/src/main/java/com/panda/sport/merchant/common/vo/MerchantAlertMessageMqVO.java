package com.panda.sport.merchant.common.vo;

import lombok.Data;


/**
 * @author javier
 * @date 2021/2/20
 */
@Data
public class MerchantAlertMessageMqVO {
    private String linkId;
    private MerchantAmountAlertVO data;
}
